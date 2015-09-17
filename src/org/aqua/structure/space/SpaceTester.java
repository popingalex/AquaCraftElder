package org.aqua.structure.space;

import java.util.ArrayList;
import java.util.List;

import test.Tester;

public class SpaceTester extends Tester {

    @Override
    public void test(String[] args) {

        SpaceRoot root = new SpaceRoot(2);
        List<int[][]> points = new ArrayList<int[][]>();
        points.add(new int[][] { { -2, -1 }, { 1, 2 } });

        // points.add(new int[][] { { 0, 0, 0 }, { 0, 1, 1 } });
        // points.add(new int[][] { { 0, 0, 0 }, { 1, 1, 0 } });
        // points.add(new int[][] { { 0, 0, 0 }, { 1, 0, 1 } });
        // points.add(new int[][] { { 0, 0, 0 }, { 1, 1, 1 } });

        System.out.println("=====================");
        for (int[][] party : points) {
            root.realloc(party[0], party[1]);
            System.out.println("=====================");
        }
        List<SpaceNode> list = root.iterateNodes(new NodeIterator()).getNodelist(true);
        System.out.println(list.size() + ":" + list);
    }

}
