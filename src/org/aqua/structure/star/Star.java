package org.aqua.structure.star;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

public class Star {
    protected Logger   logger = Logger.getLogger(getClass());
    protected Planet   center;
    public final int   dimens;
    public final int[] lower;
    public final int[] upper;
    /**
     * 
     * @param dimens 维度总数
     */
    public Star(int dimens) {
        this.dimens = dimens;
        this.lower = new int[dimens];
        this.upper = new int[dimens];
        center = new Planet(new int[dimens]);
        center.content = attachContent(center);
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
            for (int j = this.lower[i]; j < lower[i]; j++) {
                // TODO 会报错
                removeSurface(j + 1, i + this.dimens);  // 反向
            }
            for (int j = this.upper[i]; j > upper[i]; j--) {
                removeSurface(j - 1, i);
            }
            for (int j = this.lower[i]; j > lower[i]; j--) {
                attachSurface(j, i + this.dimens);      // 反向
            }
            for (int j = this.upper[i]; j < upper[i]; j++) {
                attachSurface(j, i);
            }
            this.lower[i] = lower[i];
            this.upper[i] = upper[i];
        }
    }

    private void attachSurface(int surface, int normal) {
        int[] coords = Util.getVector(normal, dimens, surface);
        Cruiser cruiser = new LocatingCruiser(coords, normal, LocatingMode.SURFACE);
        List<Planet> planetlist = cruiser.cruise();
        logger.debug("attach location:" + planetlist);
        for (Planet planet : planetlist) {    // create new Planet
            int[] offset = Util.getVector(normal, dimens, normal < dimens ? 1 : -1);
            for (int i = 0; i < dimens; i++) {
                offset[i] += planet.coords[i];
            }
            planet.rounds[normal] = new Planet(offset);
            logger.debug("attach:" + planet + "->" + planet.rounds[normal]);
            planet.rounds[normal].rounds[(normal + dimens) % dimens] = planet;
        }
        for (Planet planet : planetlist) {    // link round Planet
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
        for (Planet planet : planetlist) {
            planet.rounds[normal].content = attachContent(planet.rounds[normal]);
        }
    }

    private void removeSurface(int surface, int normal) {
        int[] coords = Util.getVector(normal, dimens, surface);
        Cruiser cruiser = new LocatingCruiser(coords, normal, LocatingMode.SURFACE);
        List<Planet> planetlist = cruiser.cruise();
        logger.debug("remove location:" + planetlist);
        for (Planet planet : planetlist) {
            int[] offset = Util.getVector(normal, dimens, normal < dimens ? 1 : -1);
            for (int i = 0; i < dimens; i++) {
                offset[i] += planet.coords[i];
            }
            removeContent(planet.rounds[normal], planet.rounds[normal].content);
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
        Planet planet = findPlanet(coords);
        if (null == planet) {
            int[] lower = this.lower.clone();
            int[] upper = this.upper.clone();
            Util.coverPoint(lower, coords, upper);
            realloc(lower, upper);
            planet = findPlanet(coords);
        }
        planet.setContent(attachPlanet(planet));
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
        Planet planet = findPlanet(coords);
        Planet.Content content = planet.getContent();
        planet.setContent(null);
        for (int i = 0; i < 3; i++) {
            if (coords[i] == upper[i] || coords[i] == lower[i]) {
                BoundingCruiser cruiser = new BoundingCruiser();
                cruiser.cruise();
                realloc(cruiser.lower, cruiser.upper);
                break;
            }
        }
        return content;
    }
    /**
     * 搜索指定位置的行星
     * @param coords
     * @return
     */
    public final Planet findPlanet(int[] coords) {
        if (Util.includePoint(lower, coords, upper)) {
            LocatingCruiser cruiser = new LocatingCruiser(coords, 0, LocatingMode.POINT);
            List<Planet> list = cruiser.cruise();
            return list.isEmpty() ? null : list.get(0);
        } else {
            return null;
        }
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
    }

    /*
     * ============================================================
     * ============================================================
     * ============================================================
     * ============================================================
     */
    /**
     * Cruiser 用于在Star中遍历Planet
     * 
     * @author Alex Xu
     *
     */
    public class Cruiser implements Comparator<Planet> {
        protected Logger logger = Logger.getLogger(getClass());

        protected boolean accept(Planet planet) {
            return true;
        }

        protected boolean interrupt(Planet planet) {
            return false;
        }

        protected void iterate(Planet planet) {
        }

        @Override
        public int compare(Planet n1, Planet n2) {
            for (int i = 0, dimens = n1.coords.length; i < dimens; i++) {
                if (n1.coords[i] != n2.coords[i]) {
                    return n1.coords[i] - n2.coords[i];
                }
            }
            return 0;
        }

        public final List<Planet> cruise() {
            return cruise(false);
        }
        /**
         * 获得复合规则的结点
         * 
         * @param star 根恒星
         * @param sort 是否排序
         * @return
         */
        public final List<Planet> cruise(boolean sort) {
            List<Planet> planetlist = new LinkedList<Planet>();
            cruise(center, dimens, planetlist);
            if (sort) {
                Collections.sort(planetlist, this);
            }
            return planetlist;
        }

        private final void cruise(Planet planet, int dimens, List<Planet> planetlist) {
            if (null == planet) {
                return;
            }
            if (accept(planet)) {                       // 回收判断
                logger.debug(planet + "accepted");
                iterate(planet);                        // 额外操作
                planetlist.add(planet);
            } else {
                logger.debug(planet + "blocked");
            }
            if (interrupt(planet)) {                    // 截断判断
                logger.debug(planet + "iterrupted");
                return;
            } else {
                logger.debug(planet + "passed");
            }
            int zero;                                   // 归零起点
            for (zero = dimens - 1; zero >= 0 && planet.coords[zero] == 0; zero--) {
            }
            if (zero >= 0) {                            // 正向遍历 TODO check zero
                Planet next = planet.rounds[planet.coords[zero] > 0 ? zero : zero + dimens];
                if (null != next && ((next.coords[zero] - planet.coords[zero]) * planet.coords[zero] > 0)) {
                    logger.debug("iter:" + planet + "->" + next);
                    cruise(next, dimens, planetlist);
                }
            }
            for (int i = zero + 1; i < dimens; i++) {   // 垂直遍历
                if (null != planet.rounds[i]) {
                    logger.debug("iter:" + planet + "->" + planet.rounds[i]);
                    cruise(planet.rounds[i], dimens, planetlist);
                }
                if (null != planet.rounds[i + dimens]) {
                    logger.debug("iter:" + planet + "->" + planet.rounds[i + dimens]);
                    cruise(planet.rounds[i + dimens], dimens, planetlist);
                }
            }
        }
    }

    public class BoundingCruiser extends Cruiser {
        private int[] lower = new int[dimens];
        private int[] upper = new int[dimens];

        public final int[] getLower() {
            return lower;
        }

        public final int[] getUpper() {
            return upper;
        }

        @Override
        public boolean accept(Planet planet) {
            return null != planet.content;
        }

        @Override
        public void iterate(Planet planet) {
            Util.coverPoint(lower, planet.coords, upper);
        }
    }

    public enum LocatingMode {
        POINT, LINE, SURFACE
    }

    public class LocatingCruiser extends Cruiser {
        private LocatingMode mode;
        private int          dimen;
        private int[]        coords;

        public LocatingCruiser(int[] coords, int dimen, LocatingMode mode) {
            this.mode = mode;
            this.dimen = dimen;
            this.coords = coords;
        }

        @Override
        public boolean accept(Planet planet) {
            int[] target = Util.shift(coords, dimens - dimen % dimens);
            int[] current = Util.shift(planet.coords, dimens - dimen % dimens);
            int branch = Util.indexBranch(current, target, 0);
            logger.debug("cur:" + Arrays.toString(current));
            logger.debug("tar:" + Arrays.toString(target));
            switch (mode) {
            case LINE:
                return branch >= dimens - 1;
            case POINT:
                return branch == dimens;
            case SURFACE:
                return current[0] == target[0];
            }
            return false;
        }

        @Override
        public boolean interrupt(Planet planet) {
            int[] target = Util.shift(coords, dimens - dimen % dimens);
            int[] current = Util.shift(planet.coords, dimens - dimen % dimens);
            int branch = Util.indexBranch(current, target, 0);
            logger.debug("cur:" + Arrays.toString(current));
            logger.debug("tar:" + Arrays.toString(target));
            logger.debug("bra:" + branch);
            switch (mode) {
            case POINT:
                if (branch == dimens) {                             // 香蕉
                    return true;
                } else if (target[branch] * current[branch] < 0) {  // 排除反向
                    return true;
                }
                for (int i = branch + 1; i < dimens - 1; i++) {     // 维度顺序
                    if (current[i] != 0) {
                        return true;
                    }
                }
                break;
            case LINE:
                if (branch >= dimens - 1) {                         // 无界
                    return false;
                } else if (target[branch] * current[branch] < 0) {  // 排除反向
                    return true;
                }
                for (int i = branch; i < dimens - 1; i++) {         // 维度顺序
                    if (current[i] != 0) {
                        return true;
                    }
                }
                break;
            case SURFACE:
                return (current[0] - target[0]) * current[0] > 0;   // 远心
            }
            return false;
        }
    }
}