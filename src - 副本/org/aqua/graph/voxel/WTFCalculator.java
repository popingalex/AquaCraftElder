package org.aqua.graph.voxel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class WTFCalculator {
    static class Result implements Comparable<Result> {
        public Integer       sum;
        public List<Integer> src;
        public Result(Integer sum, List<Integer> src) {
            super();
            this.sum = sum;
            this.src = src;
        }
        @Override
        public String toString() {
            return "Sum : [" + sum + "] " + src.toString();
        }
        @Override
        public int compareTo(Result o) {
            return sum - o.sum;
        }
        @Override
        public boolean equals(Object obj) {
            Object[] src = this.src.toArray();
            Object[] dst = ((Result) obj).src.toArray();
            Arrays.sort(src);
            Arrays.sort(dst);

            return Arrays.equals(src, dst);
        }
        // @Override
        // public int hashCode() {
        // return sum.hashCode();
        // }

    }
    static List<Result> results  = new ArrayList<Result>();
    static List<Result> results2 = new ArrayList<Result>();
    static int          threshod = 1650;
    public static void main(String[] args) {
        Integer[] src;
        src = new Integer[] { 84, 141, 189, 189, 73, 1101, 785, 768, 762, 756, 756, 377, 366, 356, 352 };
        src = new Integer[] { 141, 189, 189, 73, 1101, 785, 768, 762, 756, 756, 377, 366, 356, 352 };
        calc(Arrays.asList(src), new ArrayList<Integer>(), 0);
        System.out.println("Src : " + Arrays.toString(src));
        for (Result r : results) {
            System.out.println(r);
        }
        System.out.println("========================");
        Map<Integer, Integer> countMap = new HashMap<Integer, Integer>();
        for (Integer num : src) {
            countMap.put(num, Collections.frequency(Arrays.asList(src), num));
        }
        coll(countMap, results, new ArrayList<Result>(), 3, 0);
        for (Result r : results2) {
            System.out.println(r);
        }
    }
    private static void coll(Map<Integer, Integer> srcMap, List<Result> src, List<Result> dst, int count, int crt) {
        if (dst.size() == count) {
            if (results2.isEmpty() || sumResult(dst) < sumResult(results2)) {
                List<Integer> temp = new ArrayList<Integer>();
                for(Result r : dst) {
                    temp.addAll(r.src);
                }
                for(Entry<Integer, Integer> entry : srcMap.entrySet()) {
                    if(Collections.frequency(temp, entry.getKey()) > entry.getValue()) {
                        return;
                    }
                }
                System.out.println("rep " + sumResult(results2) + " with " + sumResult(src));
                results2 = dst;
                Collections.sort(results2);
            }
            return;
        }
//        System.out.println(dst);
        for (Result r : src) {
            List<Result> temp = new ArrayList<Result>(dst);
            temp.add(r);
            List<Result> temp2 = new ArrayList<Result>(src);
            temp2.remove(r);
            coll(srcMap, temp2, temp, 3, sumResult(temp));
        }
    }
    private static int sumResult(List<Result> results) {
        int sum = 0;
        for (Result r : results) {
            sum += r.sum;
        }
        return sum;
    }
    public static void calc(List<Integer> src, List<Integer> dst, int current) {
        if (current >= threshod && dst.size() > 2) {
            if (results.isEmpty() || current < results.get(results.size() - 1).sum) {
                Result rst = new Result(current, dst);
                if (!results.contains(rst) && dst.size() == 3) {
                    results.add(rst);
                    Collections.sort(results);
                }
            }
            return;
        }
        for (Integer num : src) {
            List<Integer> temp = new ArrayList<Integer>(dst);
            temp.add(num);
            List<Integer> temp2 = new ArrayList<Integer>(src);
            temp2.remove(num);
            calc(temp2, temp, current + num);
            // }
        }
    }
}