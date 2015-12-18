package org.aqua.structure.space;

import java.util.Arrays;

public class Planet {
    protected Content     content;

    public final Planet[] rounds;
    public final int[]    coords;
    public final int      dimens;

    public Planet(int[] coords) {
        this.coords = coords.clone();
        this.dimens = coords.length;
        this.rounds = new Planet[dimens * 2];
    }

    public final void setContent(Content content) {
        this.content = content;
    }

    public final Content getContent() {
        return content;
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