package org.aqua.struct.galaxy;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.aqua.struct.galaxy.cruiser.DimentionCruiser;
import org.aqua.struct.galaxy.cruiser.LocationCruiser;

public class Galaxy {
    protected Logger         logger = Logger.getLogger(getClass());
    protected Planet         center;
    protected int            dimens;
    public int[]             lower;
    public int[]             upper;
    public Cruiser           bounder;
    public LocationCruiser   locator;
    private DimentionCruiser dimenator;

    /**
     * 
     * @param dimens 维度总数
     */
    public Galaxy(int dimens) {
        this.dimens = dimens;
        this.lower = new int[dimens];
        this.upper = new int[dimens];
        this.center = new Planet(new int[dimens]);
        this.center.content = attachContent(center);
        this.bounder = new Cruiser(this);
        this.locator = new LocationCruiser(this);
        this.dimenator = new DimentionCruiser(this);
    }
    /**
     * 改变空间的尺寸
     * 
     * @param lower
     * @param upper
     */
    public final void realloc(int[] lower, int[] upper) {
        logger.debug("realloc:" + Arrays.toString(lower) + "-" + Arrays.toString(upper));
        for (int i = 0; i < dimens; i++) {
            lower[i] = Math.min(lower[i], 0);
            upper[i] = Math.max(upper[i], 0);
            // logger.debug("remove["+(i+this.dimens)+"]:"+this.lower[i]+"->"+lower[i]);
            for (int j = this.lower[i]; j < lower[i]; j++) {
                // TODO 会报错
                removeSurface(j + 1, i + this.dimens);  // 反向
            }
            // logger.debug("remove["+i+"]:"+this.upper[i]+"->"+upper[i]);
            for (int j = this.upper[i]; j > upper[i]; j--) {
                removeSurface(j - 1, i);
            }
            // logger.debug("attach["+(i+this.dimens)+"]:"+this.lower[i]+"->"+lower[i]);
            for (int j = this.lower[i]; j > lower[i]; j--) {
                attachSurface(j, i + this.dimens);      // 反向
            }
            // logger.debug("attach["+i+"]:"+this.upper[i]+"->"+upper[i]);
            for (int j = this.upper[i]; j < upper[i]; j++) {
                attachSurface(j, i);
            }
            this.lower[i] = lower[i];
            this.upper[i] = upper[i];
        }
    }

    private void attachSurface(int surface, int normal) {
        logger.debug("attaching :" + surface + "@" + normal);
        List<Planet> planetlist = dimenator.cruise(surface, normal);
        // logger.debug("attach location:" + planetlist);
        for (Planet planet : planetlist) {    // create new Planet
            int[] offset = Util.getVector(normal, dimens, normal < dimens ? 1 : -1);
            for (int i = 0; i < dimens; i++) {
                offset[i] += planet.coords[i];
            }
            planet.rounds[normal] = new Planet(offset);
            logger.debug("attach:" + planet + "->" + planet.rounds[normal]);
            planet.rounds[normal].rounds[(normal + dimens) % (2 * dimens)] = planet;
        }
        for (Planet planet : planetlist) {  // attach Content
            planet.rounds[normal].content = attachContent(planet.rounds[normal]);
        }
        for (Planet planet : planetlist) {  // link round Planet
            for (int i = 0, dimen = normal % dimens; i < dimens; i++) {
                if (i != dimen) {
                    if (null != planet.rounds[i]) {
                        planet.rounds[normal].rounds[i] = planet.rounds[i].rounds[normal];
                    }
                    if (null != planet.rounds[i + dimens]) {
                        planet.rounds[normal].rounds[i + dimens] = planet.rounds[i + dimens].rounds[normal];
                    }
                }
            }
        }
    }

    private void removeSurface(int surface, int normal) {
        logger.debug("removing :" + surface + "@" + normal);
        List<Planet> planetlist = dimenator.cruise(surface, normal);
        logger.debug("remove location:" + planetlist);
        for (Planet planet : planetlist) {
            int[] offset = Util.getVector(normal, dimens, normal < dimens ? 1 : -1);
            for (int i = 0; i < dimens; i++) {
                offset[i] += planet.coords[i];
            }
            // remove Content
            removeContent(planet.rounds[normal], planet.rounds[normal].content);
            // unlink Rounds
            planet.rounds[normal] = null;
        }
    }
    /**
     * 添加指定位置行星
     * 
     * @param coord3
     * @param content
     * @return
     */
    public final Planet attachPlanet(int[] coords) {
        Planet planet = locator.cruise(coords);
        if (null == planet) {
            int[] lower = this.lower.clone();
            int[] upper = this.upper.clone();
            Util.coverPoint(lower, coords, upper);
            realloc(lower, upper);
            planet = locator.cruise(coords);
        }
        System.out.println(Arrays.toString(coords));
        if (Arrays.equals(coords, new int[] { 0, 1, 1 })) {
            locator.cruise(coords);
            int i = coords[0];
            i = i + 2;
        }
        planet.content = attachPlanet(planet);
        return planet;
    }

    protected Planet.Content attachPlanet(Planet planet) {
        return null;
    }
    /**
     * 移除指定位置行星
     * 
     * @param coord3
     */
    public final Planet.Content removePlanet(int[] coords) {
        Planet planet = locator.cruise(coords);
        Planet.Content content = planet.content;
        planet.content = null;
        for (int i = 0; i < 3; i++) {
            if (coords[i] == upper[i] || coords[i] == lower[i]) {
                bounder.cruise();
                realloc(bounder.lower, bounder.upper);
                break;
            }
        }
        return content;
    }
    /**
     * 尺寸改变触发的添加行星事件
     * 
     * @param coord3
     * @param content
     * @return
     */
    protected Planet.Content attachContent(Planet planet) {
        return null;
    }
    /**
     * 尺寸改变触发的移除结点事件
     * 
     * @param planet
     */
    protected void removeContent(Planet planet, Planet.Content content) {
        planet.content = null;
    }
}