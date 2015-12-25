package org.aqua.struct.galaxy;

import org.aqua.math.Util1D;

public final class Util {
    static int indexBranch(int[] coords1, int[] coords2, int from) {
        int d = from;
        for (int dimens = coords1.length; d < dimens && coords1[d] == coords2[d]; d++) {
        }
        return d;
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

    static void coverPoint(int[] lower, int[] coords, int[] upper) {
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
