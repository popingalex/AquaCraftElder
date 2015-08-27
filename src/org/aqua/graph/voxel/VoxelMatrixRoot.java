package org.aqua.graph.voxel;

import java.util.HashMap;
import java.util.Map;

public class VoxelMatrixRoot implements VoxelMatrixConstant {

    private NodeAction     layerAttachAction;
    private NodeAction     layerLinkAction;
    public int[]           lowerPoint;
    public int[]           upperPoint;
    public VoxelMatrixNode centerNode;

    public VoxelMatrixRoot() {
        lowerPoint = new int[3];
        upperPoint = new int[3];
        centerNode = new VoxelMatrixNode();
        attachContent(centerNode);

        layerAttachAction = new NodeAction() {
            @Override
            public void act(VoxelMatrixNode node) {
                int normal6 = (Integer) source.get(SOURCE_DIRECT_NORMAL);
                VoxelMatrixNode temp = new VoxelMatrixNode();   // n : x \y/ z
                temp.coord3[normal6 % 3] = node.coord3[normal6 % 3] + (normal6 < 3 ? -1 : 1);
                temp.coord3[(normal6 + 1) % 3] = node.coord3[(normal6 + 1) % 3];
                temp.coord3[(normal6 + 2) % 3] = node.coord3[(normal6 + 2) % 3];
                attachContent(temp);
                temp.round6[(normal6 + 3) % 6] = node;          // y.neg = x
                temp.round6[normal6] = node.round6[normal6];    // y.pos = z
                if (node.round6[normal6] != null) {             // if z !+ null z.neg = y
                    node.round6[normal6].round6[(normal6 + 3) % 6] = temp;
                }
                node.round6[normal6] = temp;                    // x.pos = y
            }
        };

        layerLinkAction = new NodeAction() {
            @Override
            public void act(VoxelMatrixNode node) {
                int normal6 = (Integer) source.get(SOURCE_DIRECT_NORMAL);
                for (int n = 0, sum = node.round6.length; n < sum; n++) { // 遍历临近节点
                    if (n % 3 != normal6 % 3 && node.round6[n] != null) { // 平行于该平面且未抵达边界
                        node.round6[normal6].round6[n] = node.round6[n].round6[normal6];
                    }
                }
            }
        };
    }

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

        for (int x = this.lowerPoint[IndexX]; x > lowerPoint[IndexX]; x--) {
            attachLayer(x - 1, IndexX);
        }
        for (int x = this.upperPoint[IndexX]; x < upperPoint[IndexX]; x++) {
            attachLayer(x + 1, IndexX);
        }
        for (int y = this.lowerPoint[IndexY]; y > lowerPoint[IndexY]; y--) {
            attachLayer(y - 1, IndexY);
        }
        for (int y = this.upperPoint[IndexY]; y < upperPoint[IndexY]; y++) {
            attachLayer(y + 1, IndexY);
        }
        for (int z = this.lowerPoint[IndexZ]; z > lowerPoint[IndexZ]; z--) {
            attachLayer(z - 1, IndexZ);
        }
        for (int z = this.upperPoint[IndexZ]; z < upperPoint[IndexZ]; z++) {
            attachLayer(z + 1, IndexZ);
        }
    }

    public VoxelMatrixNode findNode(int[] coord3) {
        return centerNode != null ? centerNode.findNode(coord3) : null;
    }

    /**
     * 添加节点操作
     * 
     * @param node
     */
    public void attachContent(VoxelMatrixNode node) {
    }

    /**
     * 移除节点操作
     * 
     * @param node
     */
    public void removeContent(VoxelMatrixNode node) {
    }

    public void attachLayer(int layer, int normal3) { // layer != 0
        int[] vector = UnitVector[normal3];
        int offset = layer > 0 ? -1 : 1;
        int cutlayer = layer + offset;
        int[] coord3 = new int[] { vector[IndexX] * cutlayer, vector[IndexY] * cutlayer, vector[IndexZ] * cutlayer };
        VoxelMatrixNode node = findNode(coord3);
        // VoxelMatrixNode node = findNode(vector[IndexX] * cutlayer, vector[IndexY] * cutlayer, vector[IndexZ] *
        // cutlayer);
        if (node != null) {
            iterateLayer(normal3 + (layer > 0 ? 3 : 0), node, layerAttachAction);
            iterateLayer(normal3 + (layer > 0 ? 3 : 0), node, layerLinkAction);
        }
    }

    public void removeLayer(int layer, int asis) {
        if (layer == 0) {// 重新选中心点

        }
    }

    private void iterateLayer(int normal6, VoxelMatrixNode node, NodeAction action) {
        if (node == null)
            return;
        int priNormal = (normal6 + 1) % 3;  // primary direction on working layer
        int secNormal = (normal6 + 2) % 3;  // secondary direction on working layer

        int priComponent = node.coord3[priNormal];
        int secComponent = node.coord3[secNormal];

        if (secComponent != 0) {            // stretch on sec direction
            iterateLayer(normal6, node.round6[secComponent > 0 ? secNormal + 3 : secNormal], action);
        } else if (priComponent != 0) {     // stretch on pri and sec & -sec
            iterateLayer(normal6, node.round6[priComponent > 0 ? priNormal + 3 : priNormal], action);
            iterateLayer(normal6, node.round6[secNormal], action);
            iterateLayer(normal6, node.round6[secNormal + 3], action);
        } else {                            // stretch on both pri & -pri, sec & -sec
            iterateLayer(normal6, node.round6[priNormal], action);
            iterateLayer(normal6, node.round6[priNormal + 3], action);
            iterateLayer(normal6, node.round6[secNormal], action);
            iterateLayer(normal6, node.round6[secNormal + 3], action);
        }

        action.source.put(NodeAction.SOURCE_DIRECT_NORMAL, normal6);
        action.act(node);                   // act Node Action
    }

    public static abstract class NodeAction {

        public static String       SOURCE_DIRECT_NORMAL = "normal";
        public Map<String, Object> source               = new HashMap<String, Object>();
        public abstract void act(VoxelMatrixNode node);

    }

}
