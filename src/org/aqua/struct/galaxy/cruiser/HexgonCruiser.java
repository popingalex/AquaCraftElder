package org.aqua.struct.galaxy.cruiser;

import java.util.List;

import org.aqua.struct.galaxy.Cruiser;
import org.aqua.struct.galaxy.Galaxy;
import org.aqua.struct.galaxy.Planet;

public class HexgonCruiser extends Cruiser {

    public HexgonCruiser(Galaxy galaxy) {
        super(galaxy);
    }

    /**
     * the hexgon router works like this
     * [ ,a, ]
     * [f, ,b] -> [ ,a,b]
     * [ ,0, ] -> [f,0,c]
     * [e, ,c] -> [e,d, ]
     * [ ,d, ]
     */
    @Override
    protected void route(Planet planet, List<Planet> planetlist) {
        
    }
    
}
