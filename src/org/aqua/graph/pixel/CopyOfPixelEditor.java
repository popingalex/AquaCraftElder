package org.aqua.graph.pixel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CopyOfPixelEditor {
    static int           min      = 99999;
    static List<Integer> result;
    static int           threshod = 1650;
    public static void main(String[] args) {
        Integer[] src;
        src = new Integer[]{84, 141, 189, 189, 73, 1101, 785, 768, 762, 756, 756, 377, 366, 356, 352};
        src = new Integer[]{141, 189, 189, 73, 1101, 785, 768, 762, 756, 756, 377, 366, 356, 352};
        calc(Arrays.asList(src),
                new ArrayList<Integer>(), 0);

        System.out.println(result);
        System.out.println(min);
    }
    public static void calc(List<Integer> src, List<Integer> dst, int current) {
        if (current >= threshod && dst.size() > 2) {
            result = dst;
            return;
        }
        for (Integer num : src) {
            if (current + num < min) {
                if (current + num >= threshod) {
                    min = current + num;
                }
                List<Integer> temp = new ArrayList<Integer>(dst);
                temp.add(num);
                List<Integer> temp2 = new ArrayList<Integer>(src);
                temp2.remove(num);
                calc(temp2, temp, current + num);
            }
        }
    }
}
