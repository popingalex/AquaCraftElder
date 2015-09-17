package org.aqua.structure.space;

final class Util {
    static int indexBranch(int[] coords1, int[] coords2, int from) {
        int dimen, dimens;
        for (dimen = from, dimens = coords1.length; dimen < dimens && coords1[dimen] == coords2[dimen]; dimen++) {
        }
        return dimen;
    }

    static int[] getVector(int dimen2, int dimens, int value) {
        int[] vector = new int[dimens];
        vector[dimen2 % dimens] = value;
        return vector;
    }

    /**
     * 降低滑动
     * 
     * @param src
     * @param unit
     * @return
     */
    static int[] shift(int[] src, int unit) {
        int[] dst = new int[src.length];
        for (int i = 0, sum = src.length; i < sum; i++) {
            dst[i] = src[(sum + i - unit) % sum];
        }
        return dst;
    }

    static void packagePoint(int[] lower, int[] coords, int[] upper, int dimens) {
        for (int i = 0; i < dimens; i++) {
            lower[i] = Math.min(lower[i], coords[i]);
            upper[i] = Math.max(upper[i], coords[i]);
        }
    }

    static boolean includePoint(int[] lower, int[] coords, int[] upper, int dimens) {
        for (int i = 0; i < dimens; i++) {
            if (!between(lower[i], coords[i], upper[i])) {
                return false;
            }
        }
        return true;
    }

    private static boolean between(int lower, int coord, int upper) {
        return lower <= coord && coord <= upper;
    }
}
