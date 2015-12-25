package org.aqua.struct.galaxy;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Cruiser 用于在Galaxy中遍历Planet
 * 
 * @author Alex Xu
 *
 */
public class Cruiser {
    protected Logger   logger = Logger.getLogger(getClass());
    protected int      dimens;
    protected Planet   center;
    public List<Track> tracker;
    public boolean     track;
    public int[]       lower;
    public int[]       upper;

    public Cruiser(Galaxy galaxy) {
        this.center = galaxy.center;
        this.dimens = galaxy.dimens;
        this.tracker = new LinkedList<Track>();
        this.track = false;
        this.lower = new int[galaxy.dimens];
        this.upper = new int[galaxy.dimens];
    }

    public Planet shift(Planet planet, int... coord) {
        return null;
    }
    protected void route(Planet planet, List<Planet> planetlist) {
        int zero;
        for (zero = dimens - 1; zero >= 0 && planet.coords[zero] == 0; zero--) {
        }
        if (zero < 0) {
            // 偏离中心
        } else {
            Planet next = planet.rounds[planet.coords[zero] > 0 ? zero : zero + dimens];     // 取指定方向结点
            if (null == next) {                                                                     // 抵达边界

            } else if ((next.coords[zero] - planet.coords[zero]) * planet.coords[zero] > 0) {       // 远离原点方向
                cruise(next, planetlist);
            }
        }
        for (int i = zero + 1, count = dimens; i < count; i++) {                             // 在其它维度延伸
            if (null == planet.rounds[i]) {

            } else {
                cruise(planet.rounds[i], planetlist);
            }
            if (null == planet.rounds[i + dimens]) {

            } else {
                cruise(planet.rounds[i + dimens], planetlist);
            }
        }
    }
    /**
     * 巡航
     * 
     * @param start 起始点
     * @param current 当前点
     * @param planetlist 收集队列
     */
    private void cruise(Planet planet, List<Planet> planetlist) {
        if (accept(planet)) {       // 是否收集
            visit(planet);
            planetlist.add(planet);
            Util.coverPoint(lower, planet.coords, upper);
        } else {

        }
        if (interrupt(planet)) {    // 是否截断
            return;
        } else {
        }
        route(planet, planetlist);
    }
    
    public synchronized LinkedList<Planet> cruise() {
        LinkedList<Planet> planetlist = new LinkedList<Planet>();
        cruise(center, planetlist);
        return planetlist;
    }
    
    
    /**
     * 判断是否收集结点
     * 
     * @param planet
     * @return
     */
    protected boolean accept(Planet planet) {
        logger.debug("accept:" + planet + " " + true);
        return true;
    }
    /**
     * 判断是否中断路由
     * 
     * @param planet
     * @return
     */
    protected boolean interrupt(Planet planet) {
        logger.debug("interrupt:" + planet + " " + false);
        return false;
    }
    /**
     * 访问结点
     * 
     * @param planet
     */
    protected void visit(Planet planet) {
    }
    /**
     * 根据提供的几个点连接一条路径
     * @param routes
     * @return
     */
    public List<Track> track(Planet... routes) {
        return tracker;
    }

    public class Track {
        Planet  planet;
        boolean accept;
        boolean interrupt;

        public Track(Planet planet, boolean accept, boolean interrupt) {
            this.planet = planet;
            this.accept = accept;
            this.interrupt = interrupt;
        }
    }
}
