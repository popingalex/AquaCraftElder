package org.aqua.graph.voxel;



public class VoxelMatrixNode implements VoxelMatrixConstant {

    public VoxelMatrixNode[] adjacentNode        = new VoxelMatrixNode[6];
    public int[]             location            = new int[3];
    public Object            content             = null;

    VoxelMatrixNode findNode(int x, int y, int z) {
        VoxelMatrixNode nextNode;
        if (x != location[IndexX]) {
            nextNode = this.adjacentNode[x > location[IndexX] ? PosX : NegX];
        } else if (y != location[IndexY]) {
            nextNode = this.adjacentNode[y > location[IndexY] ? PosY : NegY];
        } else if (z != location[IndexZ]) {
            nextNode = this.adjacentNode[z > location[IndexZ] ? PosZ : NegZ];
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
