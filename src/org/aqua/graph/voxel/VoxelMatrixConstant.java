package org.aqua.graph.voxel;

public interface VoxelMatrixConstant {
    public int UnitVector[][] = { { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 } };

    public int IndexX         = 0;
    public int IndexY         = 1;
    public int IndexZ         = 2;

    public int NegX           = 0;
    public int NegY           = 1;
    public int NegZ           = 2;
    public int PosX           = 3;
    public int PosY           = 4;
    public int PosZ           = 5;
}
