package org.aqua.structure.space;

import java.util.ArrayList;
import java.util.List;

import test.Tester;

public class SpaceTester extends Tester {

    @Override
    public void test(String[] args) {
        SpaceRoot root = new SpaceRoot(2);
        List<int[][]> points = new ArrayList<int[][]>();
        // points.add(new int[][] { { -2, -1 }, { 1, 2 } });
//        points.add(new int[][] { { -1, 0 }, { 1, 0 } });
//        points.add(new int[][] { { -1, 0 }, { 1, 1 } });
//        points.add(new int[][] { { -1, 0 }, { 1, 2 } });
//        points.add(new int[][] { { -1, 0 }, { 1, 3 } });
        root = new SpaceRoot(3);
        // points.add(new int[][] { { 0, 0, 0 }, { 0, 1, 1 } });
        // points.add(new int[][] { { 0, 0, 0 }, { 1, 1, 0 } });
        // points.add(new int[][] { { 0, 0, 0 }, { 1, 0, 1 } });
        // points.add(new int[][] { { 0, 0, 0 }, { 1, 1, 1 } });
         points.add(new int[][] { { -2, 0, 0 }, { 3, 4, 1 } });
        logger.error("========================================");
        for (int[][] party : points) {
            debug(SpaceRoot.class);
            debug(LocationIterator.class);
            root.realloc(party[0], party[1]);
            logger.error("----------------------------------------");
            info(SpaceRoot.class);
            info(LocationIterator.class);
            List<SpaceNode> list = root.iterateNodes(new NodeIterator()).getNodelist(true);
            logger.info(list.size() + ":" + list);
            logger.error("========================================");
        }
    }
}
