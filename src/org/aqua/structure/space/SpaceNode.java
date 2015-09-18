package org.aqua.structure.space;

import java.util.Arrays;

public class SpaceNode {
    Content                  content;

    public final SpaceNode[] rounds;
    public final int[]       coords;
    public final int         dimens;

    public SpaceNode(int[] coords) {
        this.coords = coords;
        this.dimens = coords.length;
        this.rounds = new SpaceNode[dimens * 2];
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
        public String serialize();
        public void deserialize(String value);
    }
}