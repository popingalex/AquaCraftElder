package org.aqua.graph.voxel;

import java.util.Arrays;

import org.aqua.graph.voxel.VoxelMatrixUtil.BesiegeAction;
import org.aqua.graph.voxel.VoxelMatrixUtil.LayerAttachAction;
import org.aqua.graph.voxel.VoxelMatrixUtil.LayerLinkAction;
import org.aqua.graph.voxel.VoxelMatrixUtil.NodeAction;
import org.aqua.graph.voxel.VoxelMatrixUtil.SerializeAction;
import org.aqua.io.file.FileUtil;

public abstract class VoxelMatrixRoot implements VoxelMatrixConstant {

    private NodeAction     layerAttachAction;
    private NodeAction     layerLinkAction;
    public int[]           lowerPoint;
    public int[]           upperPoint;
    public VoxelMatrixNode centerNode;

    public VoxelMatrixRoot() {
        lowerPoint = new int[]{0,0,0};
        upperPoint = new int[]{0,0,0};

        centerNode = new VoxelMatrixNode();
        attachContent(centerNode);

        layerAttachAction = new LayerAttachAction() {

            @Override
            public void act(VoxelMatrixNode node) {
                super.act(node);
                attachContent((VoxelMatrixNode) source.get(ACTION_NODE));
            }

        };
        layerLinkAction = new LayerLinkAction();
    }

    /**
     * 添加节点操作
     * 
     * @param node
     */
    public abstract void attachContent(VoxelMatrixNode node);

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
        for (int z = this.lowerPoint[IndexZ]; z < lowerPoint[IndexZ]; z++) {
            removeLayer(z, IndexZ);
        }
        for (int z = this.upperPoint[IndexZ]; z >= upperPoint[IndexZ]; z--) {
            removeLayer(z, IndexZ);
        }
        for (int y = this.lowerPoint[IndexY]; y < lowerPoint[IndexY]; y++) {
            removeLayer(y, IndexY);
        }
        for (int y = this.upperPoint[IndexY]; y >= lowerPoint[IndexY]; y--) {
            removeLayer(y, IndexY);
        }

        for (int x = this.lowerPoint[IndexX]; x > lowerPoint[IndexX]; x--) {
            attachLayer(x - 1, IndexX);
        }
        for (int x = this.upperPoint[IndexX]; x < upperPoint[IndexX]; x++) {
            attachLayer(x + 1, IndexX);
        }
        for (int z = this.lowerPoint[IndexZ]; z > lowerPoint[IndexZ]; z--) {
            attachLayer(z - 1, IndexZ);
        }
        for (int z = this.upperPoint[IndexZ]; z < upperPoint[IndexZ]; z++) {
            attachLayer(z + 1, IndexZ);
        }
        for (int y = this.lowerPoint[IndexY]; y > lowerPoint[IndexY]; y--) {
            attachLayer(y - 1, IndexY);
            System.out.println("layer:"+y);
        }
        for (int y = this.upperPoint[IndexY]; y < upperPoint[IndexY]; y++) {
            attachLayer(y + 1, IndexY);
            System.out.println("layer:"+y);
        }

        this.lowerPoint = lowerPoint;
        this.upperPoint = upperPoint;
    }

    public VoxelMatrixNode findNode(int[] coord3) {
        return (null == centerNode) ? null : VoxelMatrixUtil.findNode(centerNode, coord3);
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
            VoxelMatrixUtil.iterateLayer(node, layerAttachAction, normal3 + (layer > 0 ? 3 : 0));
            VoxelMatrixUtil.iterateLayer(node, layerLinkAction, normal3 + (layer > 0 ? 3 : 0));
        }
    }

    public void removeLayer(int layer, int asis) {
    }

    public String exportModel() {
        NodeAction beseigeAction = new BesiegeAction();
        for (int x = lowerPoint[IndexX]; x <= upperPoint[IndexX]; x++) {
            VoxelMatrixNode layerCenterNode = VoxelMatrixUtil.findNode(centerNode, new int[] { x, 0, 0 });
            VoxelMatrixUtil.iterateLayer(layerCenterNode, beseigeAction, IndexX);
        }

        int[] lower = (int[]) beseigeAction.source.get(ACTION_LOWER);
        int[] upper = (int[]) beseigeAction.source.get(ACTION_UPPER);

        int[] size3 = new int[3];
        for (int i = 0; i < 3; i++) {
            size3[i] = upper[i] - lower[i] + 1;
        }

        int[][][] matrix = new int[size3[IndexY]][size3[IndexZ]][size3[IndexX]];
        NodeAction serializeAction = new SerializeAction();
        serializeAction.source.put(ACTION_LOWER, lower);
        serializeAction.source.put(ACTION_UPPER, upper);
        serializeAction.source.put(ACTION_MATRIX, matrix);
        for (int x = lowerPoint[IndexX]; x <= upperPoint[IndexX]; x++) {
            VoxelMatrixNode layerCenterNode = VoxelMatrixUtil.findNode(centerNode, new int[] { x, 0, 0 });
            VoxelMatrixUtil.iterateLayer(layerCenterNode, serializeAction, IndexX);
        }

        StringBuffer dataBuffer = new StringBuffer();
        dataBuffer.append(size3[IndexX]).append(" ");
        dataBuffer.append(size3[IndexY]).append(" ");
        dataBuffer.append(size3[IndexZ]).append(" ");

        char[] chars = "0123456789ABCDEF".toCharArray();

        for (int y = 0; y < size3[IndexY]; y++) {
            for (int z = 0; z < size3[IndexZ]; z++) {
                for (int x = 0; x < size3[IndexX]; x++) {
                    dataBuffer.append(chars[matrix[y][z][x]]);
                }
            }
        }

        FileUtil.makeFile("test", dataBuffer.toString());
        return dataBuffer.toString();
    }

    public void importModel(String data) {
        System.out.println(data);
        int[] size3 = new int[3];
        String[] parts = data.split(" ");
        for (int i = 0; i < 3; i++) {
            size3[i] = Integer.parseInt(parts[i]);
        }
        int[] lower = new int[] { -(size3[IndexX] - 1) / 2, 0, -(size3[IndexZ] - 1) / 2 };
        int[] upper = new int[] { size3[IndexX] / 2, size3[IndexY], size3[IndexZ] / 2 };
        System.out.println("low:" + Arrays.toString(lower));
        System.out.println("upp:" + Arrays.toString(upper));
        realloc(lower, upper);
        char[] voxels = parts[3].toCharArray();
        for (int y = 0, index = 0, z, x; y < size3[IndexY]; y++) {
            for (z = 0; z < size3[IndexZ]; z++) {
                for (x = 0; x < size3[IndexX]; x++) {
                    VoxelMatrixNode node = VoxelMatrixUtil.findNode(centerNode, new int[] {
                            x - (size3[IndexX] - 1) / 2, y, z - (size3[IndexZ] - 1) / 2 });
                    node.content.deserialize(((voxels[index++] - 48 + 6) % 23 + 10) % 16);
                }
            }
        }
    }

}
