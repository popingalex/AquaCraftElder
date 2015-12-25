package org.aqua.struct.galaxy.cruiser;

import java.util.List;

import org.aqua.struct.galaxy.Cruiser;
import org.aqua.struct.galaxy.Galaxy;
import org.aqua.struct.galaxy.Planet;

public class DimentionCruiser extends Cruiser {
    private int coord;
    private int normal;
    public DimentionCruiser(Galaxy galaxy) {
        super(galaxy);
    }
    public synchronized List<Planet> cruise(int coord, int normal) {
        this.coord = coord;
        this.normal = normal % dimens;
        return cruise();
    }
    @Override
    protected boolean accept(Planet planet) {
        logger.debug("accept:" + planet + " " + (coord == planet.coords[normal]));
        return coord == planet.coords[normal];
    }
    @Override
    protected boolean interrupt(Planet planet) {
        logger.debug("interrupt:" + planet + " " + (planet.coords[normal] * coord < 0));
        if (planet.coords[normal] * coord < 0) {                                                // 维度相反
            return true;
        } else {
            return false;
        }
    }
}