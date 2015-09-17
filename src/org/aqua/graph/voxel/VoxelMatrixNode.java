package org.aqua.graph.voxel;

public class VoxelMatrixNode implements VoxelMatrixConstant {

    public VoxelMatrixNode[]  round6  = new VoxelMatrixNode[6];
    public int[]              coord3  = new int[3];
    public VoxelMatrixContent content = null;

    public static interface VoxelMatrixContent {

        public int serialize();

        public void deserialize(int value);

    }
}
