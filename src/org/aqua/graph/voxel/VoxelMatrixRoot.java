package org.aqua.graph.voxel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.aqua.graph.j3d.VoxelMatrixViewer;

public class VoxelMatrixRoot implements VoxelMatrixConstant {

    public static NodeAction ACTION_REMARK_LAYER = new RemarkLayerAction();
    public static NodeAction ACTION_ATTACH_LAYER = new AttachLayerAction();
    public static NodeAction ACTION_LINK_LAYER   = new LinkLayerAction();

    public int[]             lowerPoint          = new int[3];
    public int[]             upperPoint          = new int[3];

    public VoxelMatrixNode   centerNode          = new VoxelMatrixNode();

    /**
     * always keep a cube
     * 
     * @param minX
     * @param minY
     * @param minZ
     * @param maxX
     * @param maxY
     * @param maxZ
     */
    public void realloc(int[] lowerPoint, int[] upperPoint) {
        lowerPoint[IndexX] = Math.min(lowerPoint[IndexX], 0);   // get minimum bound
        lowerPoint[IndexY] = Math.min(lowerPoint[IndexY], 0);
        lowerPoint[IndexZ] = Math.min(lowerPoint[IndexZ], 0);

        upperPoint[IndexX] = Math.max(upperPoint[IndexX], 0);
        upperPoint[IndexY] = Math.max(upperPoint[IndexY], 0);
        upperPoint[IndexZ] = Math.max(upperPoint[IndexZ], 0);

        for (int x = this.lowerPoint[IndexX]; x < lowerPoint[IndexX]; x++) {
            removeLayer(x, IndexX);
        }
        for (int x = this.upperPoint[IndexX]; x >= upperPoint[IndexX]; x--) {
            removeLayer(x, IndexX);
        }
        for (int y = this.lowerPoint[IndexY]; y < lowerPoint[IndexY]; y++) {
            removeLayer(y, IndexY);
        }
        for (int y = this.upperPoint[IndexY]; y >= lowerPoint[IndexY]; y--) {
            removeLayer(y, IndexY);
        }
        for (int z = this.lowerPoint[IndexZ]; z < lowerPoint[IndexZ]; z++) {
            removeLayer(z, IndexZ);
        }
        for (int z = this.upperPoint[IndexZ]; z >= upperPoint[IndexZ]; z--) {
            removeLayer(z, IndexZ);
        }
        System.out.println(Arrays.toString(lowerPoint) + "_" + Arrays.toString(upperPoint));

        System.out.println("Attach Layer : " + this.lowerPoint[IndexX] + ">>" + (lowerPoint[IndexX] + 1));
        for (int x = this.lowerPoint[IndexX]; x > lowerPoint[IndexX]; x--) {
            attachLayer(x - 1, IndexX);
        }
        System.out.println("Attach Layer : " + this.upperPoint[IndexX] + "<<" + (upperPoint[IndexX] - 1));
        for (int x = this.upperPoint[IndexX]; x < upperPoint[IndexX]; x++) {
            attachLayer(x + 1, IndexX);
        }
        System.out.println("Attach Layer : " + this.lowerPoint[IndexY] + ">>" + (lowerPoint[IndexY] + 1));
        for (int y = this.lowerPoint[IndexY]; y > lowerPoint[IndexY]; y--) {
            attachLayer(y - 1, IndexY);
        }
        System.out.println("Attach Layer : " + this.upperPoint[IndexY] + "<<" + (upperPoint[IndexY] - 1));
        for (int y = this.upperPoint[IndexY]; y < upperPoint[IndexY]; y++) {
            attachLayer(y + 1, IndexY);
        }
        System.out.println("Attach Layer : " + this.lowerPoint[IndexZ] + ">>" + (lowerPoint[IndexZ] + 1));
        for (int z = this.lowerPoint[IndexZ]; z > lowerPoint[IndexZ]; z--) {
            attachLayer(z - 1, IndexZ);
        }
        System.out.println("Attach Layer : " + this.upperPoint[IndexZ] + "<<" + (upperPoint[IndexZ] - 1));
        for (int z = this.upperPoint[IndexZ]; z < upperPoint[IndexZ]; z++) {
            attachLayer(z + 1, IndexZ);
        }

        System.out.println(centerNode);
    }

    public VoxelMatrixNode findNode(int x, int y, int z) {
        return centerNode != null ? centerNode.findNode(x, y, z) : null;
    }

    public void attachNode(int coordX, int coordY, int coordZ, Object content) {

    }

    public Object removeNode(int coordX, int coordY, int coordZ) {
        return null;
    }

    public void attachLayer(int layer, int normal3) { // layer != 0
        int[] vector = UnitVector[normal3];
        int offset = layer > 0 ? -1 : 1;
        int cutlayer = layer + offset;
        System.out.println("cutlayer: " + cutlayer);
        VoxelMatrixNode node = findNode(vector[IndexX] * cutlayer, vector[IndexY] * cutlayer, vector[IndexZ] * cutlayer);
        System.out.println("node found?:" + node != null);
        if (node != null) {
            System.out.println("layer   : " + layer);
            iterateLayer(normal3 + (layer > 0 ? 3 : 0), node, ACTION_ATTACH_LAYER);
            iterateLayer(normal3 + (layer > 0 ? 3 : 0), node, ACTION_LINK_LAYER);

            // TODO new center?

            // iterateLayer(normal3, centerNode, ACTION_REMARK_LAYER);
            // for (VoxelMatrixNode n = centerNode; n.adjacentNode[PosZ] != null; n = n.adjacentNode[PosZ]) {
            // n.adjacentNode[PosZ].location[IndexX] = 0;
            // n.adjacentNode[PosZ].location[IndexY] = 0;
            // n.adjacentNode[PosZ].location[IndexZ] = n.location[IndexZ + 1];
            // iterateLayer(normal3, n.adjacentNode[PosZ], ACTION_REMARK_LAYER);
            // }
            //
            // for (VoxelMatrixNode n = centerNode; n.adjacentNode[NegZ] != null; n = n.adjacentNode[NegZ]) {
            // n.adjacentNode[NegZ].location[IndexX] = 0;
            // n.adjacentNode[NegZ].location[IndexY] = 0;
            // n.adjacentNode[NegZ].location[IndexZ] = n.location[IndexZ - 1];
            // iterateLayer(normal3, n.adjacentNode[NegZ], ACTION_REMARK_LAYER);
            // }
        }
    }

    public void removeLayer(int layer, int asis) {
        if (layer == 0) {// 重新选中心点

        }
    }

    private void iterateLayer(int normal6, VoxelMatrixNode node, NodeAction action) {
        if (node == null)
            return;
        int priNormal = (normal6 + 1) % 3;          // primary direction on working layer
        int secNormal = (normal6 + 2) % 3;          // secondary direction on working layer

        // if(!action.source.containsKey(NodeAction.SOURCE_RECT_LAYERX)) { // It's root
        //
        // }

        int priComponent = node.location[priNormal];
        int secComponent = node.location[secNormal];

        if (secComponent != 0) {                    // stretch on sec direction
            iterateLayer(normal6, node.adjacentNode[secComponent > 0 ? secNormal + 3 : secNormal], action);
        } else if (priComponent != 0) {             // stretch on pri and sec & -sec
            iterateLayer(normal6, node.adjacentNode[priComponent > 0 ? priNormal + 3 : priNormal], action);
            iterateLayer(normal6, node.adjacentNode[secNormal], action);
            iterateLayer(normal6, node.adjacentNode[secNormal + 3], action);
        } else {                                    // stretch on both pri & -pri, sec & -sec
            iterateLayer(normal6, node.adjacentNode[priNormal], action);
            iterateLayer(normal6, node.adjacentNode[priNormal + 3], action);
            iterateLayer(normal6, node.adjacentNode[secNormal], action);
            iterateLayer(normal6, node.adjacentNode[secNormal + 3], action);
        }

        action.source.put(NodeAction.SOURCE_DIRECT_NORMAL, normal6);
        action.act(node);                           // act Node Action
    }

    public static void main(String[] args) {
        VoxelMatrixRoot root = new VoxelMatrixRoot();
        root.realloc(new int[] { -1, 0, -1 }, new int[] { 1, 0, 1 });

    }

    private static class AttachLayerAction extends NodeAction {

        @Override
        public void act(VoxelMatrixNode node) {
            int normal6 = (Integer) source.get(SOURCE_DIRECT_NORMAL);
            int negNormal = (normal6 + 3) % 6;

            VoxelMatrixNode temp = new VoxelMatrixNode();                   // n : x \y/ z
            {
                float size = 0.4f;
                temp.content = new VoxelMatrixViewer.VoxelBox(size, size, size);
            }
            temp.location[normal6 % 3] = node.location[normal6 % 3] + (normal6 < 3 ? -1 : 1);
            temp.location[(normal6 + 1) % 3] = node.location[(normal6 + 1) % 3];
            temp.location[(normal6 + 2) % 3] = node.location[(normal6 + 2) % 3];
            temp.adjacentNode[negNormal] = node;                            // y.neg = x
            temp.adjacentNode[normal6] = node.adjacentNode[normal6];        // y.pos = z
            if (node.adjacentNode[normal6] != null) {                       // if z !+ null
                node.adjacentNode[normal6].adjacentNode[negNormal] = temp;  // z.neg = y
            }
            node.adjacentNode[normal6] = temp;                              // x.pos = y

            // System.out.println(Arrays.toString(temp.location));
        }

    };

    private static class LinkLayerAction extends NodeAction {

        @Override
        public void act(VoxelMatrixNode node) {
            int normal6 = (Integer) source.get(SOURCE_DIRECT_NORMAL);

            for (int n = 0, sum = node.adjacentNode.length; n < sum; n++) { // 遍历临近节点
                if (n % 3 != normal6 % 3 && node.adjacentNode[n] != null) { // 平行于该平面且未抵达边界
                    node.adjacentNode[normal6].adjacentNode[n] = node.adjacentNode[n].adjacentNode[normal6];
                }
            }
        }

    }

    private static class RemarkLayerAction extends NodeAction {

        @Override
        public void act(VoxelMatrixNode node) {
            int normal6 = (Integer) source.get(SOURCE_DIRECT_NORMAL);

            if (node.adjacentNode[normal6] != null) {
                node.adjacentNode[normal6].location[normal6 % 3] = node.location[normal6 % 3] + (normal6 < 3 ? -1 : 1);
            }
        }

    }

    public static abstract class NodeAction {

        public static String       SOURCE_RECT_LAYERX   = "layerx";
        public static String       SOURCE_RECT_LAYERY   = "layery";
        public static String       SOURCE_DIRECT_NORMAL = "normal";
        public Map<String, Object> source               = new HashMap<String, Object>();

        public abstract void act(VoxelMatrixNode node);

    }
}