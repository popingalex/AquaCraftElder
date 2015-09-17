package org.aqua.structure.space;

import java.util.List;

public class SpaceRoot {
    protected SpaceNode center;
    public final int    dimens;
    public final int[]  lower;
    public final int[]  upper;

    /**
     * 
     * @param dimens 维度总数
     */
    public SpaceRoot(int dimens) {
        this.dimens = dimens;
        this.lower = new int[dimens];
        this.upper = new int[dimens];
        center = new SpaceNode(new int[dimens]);
    }

    /**
     * 改变空间的尺寸
     * 
     * @param lower
     * @param upper
     */
    public final void realloc(int[] lower, int[] upper) {
        for (int i = 0; i < dimens; i++) {
            lower[i] = Math.min(lower[i], 0);
            upper[i] = Math.max(upper[i], 0);
            // TODO remove还没做过.
            for (int j = this.lower[i]; j < lower[i]; j++) {
                removeSurface(j + 1, i + this.dimens);  // 反向
            }
            for (int j = this.upper[i]; j > upper[i]; j--) {
                removeSurface(j - 1, i);
            }
            for (int j = this.lower[i]; j > lower[i]; j--) {
                attachSurface(j, i + this.dimens);      // 反向
            }
            for (int j = this.upper[i]; j < upper[i]; j++) {
                attachSurface(j, i);
            }
            this.lower[i] = lower[i];
            this.upper[i] = upper[i];
        }
    }
    public void attachSurface(int surface, int dimen2) {
        int[] coords = Util.getVector(dimen2, dimens, surface);
        NodeIterator iterator = iterateNodes(new LocationIterator(coords, dimen2, LocationIterator.Mode.SURFACE));
        List<SpaceNode> nodelist = iterator.getNodelist();
        for (SpaceNode node : nodelist) {    // create new Node
            int[] offset = Util.getVector(dimen2, dimens, dimen2 < dimens ? 1 : -1);
            for (int i = 0; i < dimens; i++) {
                offset[i] += node.coords[i];
            }
            node.rounds[dimen2] = new SpaceNode(offset);
            node.rounds[dimen2].rounds[(dimen2 + dimens) % dimens] = node;
        }
        for (SpaceNode node : nodelist) {    // link round Node
            for (int i = 0, dimen = dimen2 % dimens; i < dimens; i++) {
                if (i != dimen) {
                    if (null != node.rounds[i]) {
                        node.rounds[dimen2].rounds[i] = node.rounds[i].rounds[dimen2];
                    }
                    if (null != node.rounds[i + dimens]) {
                        node.rounds[dimen2].rounds[i + dimens] = node.rounds[i + dimens].rounds[dimen2];
                    }
                }
            }
        }
        for (SpaceNode node : nodelist) {
            node.content = attachContent(node);
        }
    }

    public void removeSurface(int surface, int dimen2) {

    }

    /**
     * 从外部添加结点
     * 
     * @param coord3
     * @param content
     * @return
     */
    public final SpaceNode attachNode(int[] coord, SpaceNode.Content content) {
        return null;
    }

    /**
     * 从外部移除结点
     * 
     * @param coord3
     */
    public final SpaceNode.Content removeNode(int[] coord) {
        return null;
    }

    public final SpaceNode findNode(int[] coords) {
        if (Util.includePoint(lower, coords, upper, dimens)) {
            LocationIterator iterator = new LocationIterator(coords, 0, LocationIterator.Mode.POINT);
            iterateNodes(iterator);
            List<SpaceNode> list = iterator.getNodelist();
            return list.isEmpty() ? null : list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 获得复合规则的结点
     * 
     * @param iterator
     * @return
     */
    public final synchronized NodeIterator iterateNodes(NodeIterator iterator) {
        iterator.getNodelist().clear();
        iterateNodes(center, iterator);
        return iterator;
    }

    private final void iterateNodes(SpaceNode node, NodeIterator iterator) {
        if (null == node) {
            return;
        }
        if (iterator.accept(node)) {                // 回收判断
            iterator.iterate(node);                 // 额外操作
            iterator.getNodelist().add(node);
        } else {
        }
        if (iterator.stop(node)) {                  // 截断判断
            return;
        } else {
        }
        int zero;                                   // 归零起点
        for (zero = dimens - 1; zero >= 0 && node.coords[zero] == 0; zero--) {
        }
        if (zero >= 0) {                            // 正向遍历 TODO check zero
            SpaceNode next = node.rounds[node.coords[zero] > 0 ? zero : zero + dimens];
            if (null != next && ((next.coords[zero] - node.coords[zero]) * node.coords[zero] > 0)) {
                iterateNodes(next, iterator);
            }
        }
        for (int i = zero + 1; i < dimens; i++) {   // 垂直遍历
            if (null != node.rounds[i]) {
                iterateNodes(node.rounds[i], iterator);
            }
            if (null != node.rounds[i + dimens]) {
                iterateNodes(node.rounds[i + dimens], iterator);
            }
        }
    }
    /**
     * 尺寸改变触发的添加结点事件
     * 
     * @param coord3
     * @param content
     * @return
     */
    public SpaceNode.Content attachContent(SpaceNode node) {
        return null;
    }

    /**
     * 尺寸改变触发的移除结点事件
     * 
     * @param node
     */
    public void removeContent(SpaceNode node, SpaceNode.Content content) {
    }
}
