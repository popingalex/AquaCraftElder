package org.aqua.graph.voxel;

public class VoxelMatrixNode implements VoxelMatrixConstant {

    public VoxelMatrixNode[] round6  = new VoxelMatrixNode[6];
    public int[]             coord3  = new int[3];
    public Object            content = null;

    VoxelMatrixNode findNode(int[] targetCoord3) {
        VoxelMatrixNode nextNode;
        if (targetCoord3[IndexX] != coord3[IndexX]) {
            nextNode = this.round6[targetCoord3[IndexX] > coord3[IndexX] ? PosX : NegX];
        } else if (targetCoord3[IndexY] != coord3[IndexY]) {
            nextNode = this.round6[targetCoord3[IndexY] > coord3[IndexY] ? PosY : NegY];
        } else if (targetCoord3[IndexZ] != coord3[IndexZ]) {
            nextNode = this.round6[targetCoord3[IndexZ] > coord3[IndexZ] ? PosZ : NegZ];
        } else {
            return this;
        }

        if (nextNode != null) {
            return nextNode.findNode(targetCoord3);
        } else {
            return null;
        }
    }
}
