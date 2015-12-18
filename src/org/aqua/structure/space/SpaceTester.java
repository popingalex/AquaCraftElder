package org.aqua.structure.space;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import demo.Tester;

public class SpaceTester extends Tester {

    @Override
    public void test(String[] args) {
        Star root = new Star(2);
        List<int[][]> points = new ArrayList<int[][]>();
        // points.add(new int[][] { { -2, -1 }, { 1, 2 } });
        // points.add(new int[][] { { -1, 0 }, { 1, 0 } });
        // points.add(new int[][] { { -1, 0 }, { 1, 1 } });
        // points.add(new int[][] { { -1, 0 }, { 1, 2 } });
        // points.add(new int[][] { { -1, 0 }, { 1, 3 } });
        root = new Star(3);
        // points.add(new int[][] { { 0, 0, 0 }, { 0, 1, 1 } });
        // points.add(new int[][] { { 0, 0, 0 }, { 1, 1, 0 } });
        // points.add(new int[][] { { 0, 0, 0 }, { 1, 0, 1 } });
        points.add(new int[][] { { -1, 0, 0 }, { 1, 1, 1 } });
        points.add(new int[][] { { -2, 0, 0 }, { 3, 4, 1 } });
        points.add(new int[][] { { -1, 0, 0 }, { 1, 1, 1 } });
        logger.error("========================================");
        for (int[][] party : points) {
            // debug(Star.class);
            // debug(LocationCruiser.class);
            root.realloc(party[0], party[1]);
            logger.error("----------------------------------------");
            info(Star.class);
            List<Planet> list = root.new Cruiser().cruise();
            logger.info("l:" + Arrays.toString(root.lower) + "," + "u:" + Arrays.toString(root.upper));
            logger.info(list.size() + ":" + list);
            logger.error("========================================");
        }
    }
}
