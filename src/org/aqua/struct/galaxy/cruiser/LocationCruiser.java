package org.aqua.struct.galaxy.cruiser;

import java.util.Arrays;

import org.aqua.struct.galaxy.Cruiser;
import org.aqua.struct.galaxy.Galaxy;
import org.aqua.struct.galaxy.Planet;

public class LocationCruiser extends Cruiser {
    public LocationCruiser(Galaxy galaxy) {
        super(galaxy);
    }
    private int[] target;
    public synchronized Planet cruise(int[] target) {
        this.target = target;
        return cruise().peek();
    }
    @Override
    protected boolean accept(Planet planet) {
        logger.debug("accept:" + planet + " " + Arrays.equals(planet.coords, target));
        return Arrays.equals(planet.coords, target);
    }
    @Override
    protected boolean interrupt(Planet planet) {
        int sep, zero;
        for (sep = 0; sep < dimens && planet.coords[sep] == target[sep]; sep++) {        // 取分歧点
        }
        for (zero = sep + 1; zero < dimens && planet.coords[zero] == 0; zero++) {
        }
        if (sep == dimens) {                                                             // 抵达目标点
            logger.debug("interrupt[0]:" + planet + " " + true);
            return true;
        } else if (planet.coords[sep] * target[sep] < 0) {                                      // 方向错了
            logger.debug("interrupt[1]:" + planet + " " + true);
            return true;
        } else if (zero == dimens) {
            logger.debug("interrupt[2]:" + planet + " " + false);
            return false;
        } else {                                                                                // 谜之分叉
            logger.debug("interrupt[3]:" + planet + " " + true);
            return true;
        }
    }
}