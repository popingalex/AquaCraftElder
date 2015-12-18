package org.aqua.structure.space;

import org.aqua.math.Util1D;

public final class Util {
    static int indexBranch(int[] coords1, int[] coords2, int from) {
        int dimen, dimens;
        for (dimen = from, dimens = coords1.length; dimen < dimens && coords1[dimen] == coords2[dimen]; dimen++) {
        }
        return dimen;
    }

    static int[] getVector(int dimen, int dimens, int value) {
        int[] vector = new int[dimens];
        vector[dimen % dimens] = value;
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

    public static void coverPoint(int[] lower, int[] coords, int[] upper) {
        for (int i = 0; i < coords.length; i++) {
            lower[i] = Math.min(lower[i], coords[i]);
            upper[i] = Math.max(upper[i], coords[i]);
        }
    }

    static boolean includePoint(int[] lower, int[] coords, int[] upper) {
        for (int i = 0; i < coords.length; i++) {
            if (!Util1D.between(lower[i], coords[i], upper[i])) {
                return false;
            }
        }
        return true;
    }
}
