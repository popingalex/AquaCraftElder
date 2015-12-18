package org.aqua.structure.path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Path {

    public Path(int[][] source) {
        List<int[][]> results = new ArrayList<int[][]>();
        source[0][0] = 2;
        iterator(source, 0, 0, 25, results);
        System.out.println(results.size());
        for (int[][] result : results) {
            for (int[] line : result) {
                System.out.println(Arrays.toString(line));
            }
            System.out.println("--------------------");
        }
    }

    public void iterator(int[][] source, int x, int y, int max, List<int[][]> result) {
        if (source[y][x] < max) {
            int[][] temp;
            if (y > 0 && source[y - 1][x] == 0) {
                temp = source.clone();
                temp[y - 1][x] = source[y][x] + 1;
                iterator(temp, x, y - 1, max, result);
            }
            if (x > 0 && source[y][x - 1] == 0) {
                temp = source.clone();
                temp[y][x - 1] = source[y][x] + 1;
                iterator(temp, x - 1, y, max, result);
            }
            if (x < source[0].length - 1 && source[y][x + 1] == 0) {
                temp = source.clone();
                temp[y][x + 1] = source[y][x] + 1;
                iterator(temp, x + 1, y, max, result);
            }
            if (y < source[0].length - 1 && source[y + 1][x] == 0) {
                temp = source.clone();
                temp[y + 1][x] = source[y][x] + 1;
                iterator(temp, x, y + 1, max, result);
            }
        } else {
            result.add(source);
        }
    }

    public static void main(String[] args) {
        int[][] source = { { 0, 1, 0, 0, 0 }, { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0 } };
        new Path(source);
    }

}
