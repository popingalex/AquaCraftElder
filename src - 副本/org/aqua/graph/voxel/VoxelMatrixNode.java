package org.aqua.graph.voxel;

public class VoxelMatrixNode implements VoxelMatrixConstant {

    public VoxelMatrixNode[] adjacentNode = new VoxelMatrixNode[6];
    public int[]             coordinate3  = new int[3];
    public Object            content      = null;

    VoxelMatrixNode findNode(int x, int y, int z) {
        VoxelMatrixNode nextNode;
        if (x != coordinate3[IndexX]) {
            nextNode = this.adjacentNode[x > coordinate3[IndexX] ? PosX : NegX];
        } else if (y != coordinate3[IndexY]) {
            nextNode = this.adjacentNode[y > coordinate3[IndexY] ? PosY : NegY];
        } else if (z != coordinate3[IndexZ]) {
            nextNode = this.adjacentNode[z > coordinate3[IndexZ] ? PosZ : NegZ];
        } else {
            return this;
        }

        if (nextNode != null) {
            return nextNode.findNode(x, y, z);
        } else {
            return null;
        }
    }

}
