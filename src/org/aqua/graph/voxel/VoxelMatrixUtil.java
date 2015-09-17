package org.aqua.graph.voxel;

import java.util.HashMap;
import java.util.Map;

public class VoxelMatrixUtil implements VoxelMatrixConstant {

    public static void packagePoint(int[] lower, int[] upper, int[] coord3) {
        for (int i = 0; i < 3; i++) {
            lower[i] = Math.min(lower[i], coord3[i]);
            upper[i] = Math.max(upper[i], coord3[i]);
        }
    }

    public static boolean includePoint(int[] lower, int[] upper, int[] coord3) {
        for (int i = 0; i < 3; i++) {
            if (!between(lower[i], coord3[i], upper[i])) {
                return false;
            }
        }
        return true;
    }

    private static boolean between(int lower, int coord, int upper) {
        return lower <= coord && coord <= upper;
    }

    // TODO 把findNode和IterateLayer合并
    public static VoxelMatrixNode findNode(VoxelMatrixNode node, int[] coord3) {
        VoxelMatrixNode nextNode = null;
        if (coord3[IndexX] != node.coord3[IndexX]) {
            nextNode = node.round6[(coord3[IndexX] > node.coord3[IndexX] ? PosX : NegX)];
        } else if (coord3[IndexY] != node.coord3[IndexY]) {
            nextNode = node.round6[(coord3[IndexY] > node.coord3[IndexY] ? PosY : NegY)];
        } else if (coord3[IndexZ] != node.coord3[IndexZ]) {
            nextNode = node.round6[(coord3[IndexZ] > node.coord3[IndexZ] ? PosZ : NegZ)];
        } else {
            return node;
        }
        return (null == nextNode) ? null : findNode(nextNode, coord3);
    }

    public static void iterateLayer(VoxelMatrixNode node, NodeAction action, int normal6) {
        if (node == null)
            return;
        int priNormal = (normal6 + 1) % 3;  // primary direction on working layer
        int secNormal = (normal6 + 2) % 3;  // secondary direction on working layer

        int priComponent = node.coord3[priNormal];
        int secComponent = node.coord3[secNormal];

        if (secComponent != 0) {            // stretch on sec direction
            iterateLayer(node.round6[secComponent > 0 ? secNormal + 3 : secNormal], action, normal6);
        } else if (priComponent != 0) {     // stretch on pri and sec & -sec
            iterateLayer(node.round6[priComponent > 0 ? priNormal + 3 : priNormal], action, normal6);
            iterateLayer(node.round6[secNormal], action, normal6);
            iterateLayer(node.round6[secNormal + 3], action, normal6);
        } else {                            // stretch on both pri & -pri, sec & -sec
            iterateLayer(node.round6[priNormal], action, normal6);
            iterateLayer(node.round6[priNormal + 3], action, normal6);
            iterateLayer(node.round6[secNormal], action, normal6);
            iterateLayer(node.round6[secNormal + 3], action, normal6);
        }

        action.source.put(ACTION_NORMAL, normal6);
        action.act(node);                   // act Node Action
    }

    public static abstract class NodeAction {

        public Map<String, Object> source = new HashMap<String, Object>();

        public abstract void act(VoxelMatrixNode node);

    }

    public static class LayerLinkAction extends NodeAction {
        @Override
        public void act(VoxelMatrixNode node) {
            int normal6 = (Integer) source.get(ACTION_NORMAL);
            for (int n = 0, sum = node.round6.length; n < sum; n++) { // 遍历临近节点
                if (n % 3 != normal6 % 3 && node.round6[n] != null) { // 平行于该平面且未抵达边界
                    node.round6[normal6].round6[n] = node.round6[n].round6[normal6];
                }
            }
        }
    }

    public static class LayerAttachAction extends NodeAction {
        @Override
        public void act(VoxelMatrixNode node) {
            int normal6 = (Integer) source.get(ACTION_NORMAL);
            VoxelMatrixNode temp = new VoxelMatrixNode();   // n : x \y/ z
            temp.coord3[normal6 % 3] = node.coord3[normal6 % 3] + (normal6 < 3 ? -1 : 1);
            temp.coord3[(normal6 + 1) % 3] = node.coord3[(normal6 + 1) % 3];
            temp.coord3[(normal6 + 2) % 3] = node.coord3[(normal6 + 2) % 3];
            source.put(ACTION_NODE, temp);
            temp.round6[(normal6 + 3) % 6] = node;          // y.neg = x
            temp.round6[normal6] = node.round6[normal6];    // y.pos = z
            if (node.round6[normal6] != null) {             // if z !+ null z.neg = y
                node.round6[normal6].round6[(normal6 + 3) % 6] = temp;
            }
            node.round6[normal6] = temp;                    // x.pos = y
        }
    }

    public static class DeserializeAction extends NodeAction {

        @Override
        public void act(VoxelMatrixNode node) {
            // TODO Auto-generated method stub

        }

    }

    public static class SerializeAction extends NodeAction {

        @Override
        public void act(VoxelMatrixNode node) {
            if (node.content.serialize() > 0) {
                int[] lower = (int[]) source.get(ACTION_LOWER);
                int[] upper = (int[]) source.get(ACTION_UPPER);
                if (includePoint(lower, upper, node.coord3)) {
                    int[][][] matrix = (int[][][]) source.get(ACTION_MATRIX);
                    int offsetX = node.coord3[IndexX] - lower[IndexX];
                    int offsetY = node.coord3[IndexY] - lower[IndexY];
                    int offsetZ = node.coord3[IndexZ] - lower[IndexZ];
                    matrix[offsetY][offsetZ][offsetX] = node.content.serialize();
                }
            }
        }

    }

    /**
     * 获得最小包络坐标
     * 
     * @author Alex Xu
     */
    public static class BesiegeAction extends NodeAction {

        public BesiegeAction() {
            source.put(ACTION_LOWER, new int[3]);
            source.put(ACTION_UPPER, new int[3]);
        }

        @Override
        public void act(VoxelMatrixNode node) {
            if (node.content.serialize() > 0) {
                int[] lower = (int[]) source.get(ACTION_LOWER);
                int[] upper = (int[]) source.get(ACTION_UPPER);
                packagePoint(lower, upper, node.coord3);
            }
        }

    }
}
