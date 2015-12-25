package org.aqua.struct.galaxy;

import java.util.Arrays;
import java.util.HashMap;

public class Planet {
    public Content                        content;
    public final HashMap<Integer, Planet> edgemap;
    public final Planet[]                 rounds;
    public final int[]                    coords;
    public final int                      dimens;

    public Planet(int[] coords) {
        this.edgemap = new HashMap<Integer, Planet>();
        this.coords = coords.clone();
        this.dimens = coords.length;
        this.rounds = new Planet[dimens * 2];
    }

    @Override
    public String toString() {
        return Arrays.toString(coords);
    }

    public static interface Content {
        public Object serialize();
        public void deserialize(Object data);
    }
}