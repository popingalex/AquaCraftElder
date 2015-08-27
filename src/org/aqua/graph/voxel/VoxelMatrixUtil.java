package org.aqua.graph.voxel;

public class VoxelMatrixUtil {

    public static void packagePoints(int[] lower, int[] upper, int[] target) {
        for (int i = 0; i < 3; i++) {
            lower[i] = Math.min(lower[i], target[i]);
            upper[i] = Math.max(upper[i], target[i]);
        }
    }

}
