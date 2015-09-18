package org.aqua.structure.space;

import java.util.Arrays;

public class LocationIterator extends NodeIterator {
    public enum Mode {
        POINT, LINE, SURFACE
    }

    private Mode  mode;
    private int   dimen;
    private int   dimens;
    private int[] coords;

    public LocationIterator(int[] coords, int dimen, Mode mode) {
        this.mode = mode;
        this.dimen = dimen;
        this.coords = coords;
        this.dimens = coords.length;
    }

    @Override
    public boolean accept(SpaceNode node) {
        int[] target = Util.shift(coords, dimens - dimen % dimens);
        int[] current = Util.shift(node.coords, dimens - dimen % dimens);
        int branch = Util.indexBranch(current, target, 0);
        logger.debug("cur:" + Arrays.toString(current));
        logger.debug("tar:" + Arrays.toString(target));
        switch (mode) {
        case LINE:
            return branch >= dimens - 1;
        case POINT:
            return branch == dimens;
        case SURFACE:
            return current[0] == target[0];
        }
        return false;
    }

    @Override
    public boolean interrupt(SpaceNode node) {
        int[] target = Util.shift(coords, dimens - dimen % dimens);
        int[] current = Util.shift(node.coords, dimens - dimen % dimens);
        int branch = Util.indexBranch(current, target, 0);
        logger.debug("cur:" + Arrays.toString(current));
        logger.debug("tar:" + Arrays.toString(target));
        logger.debug("bra:" + branch);
        switch (mode) {
        case POINT:
            if (branch == dimens) {                                                     // 香蕉
                return true;
            } else if (target[branch] * current[branch] < 0) {                          // 排除反向
                return true;
            }
            for (int i = branch + 1; i < dimens - 1; i++) {                             // 维度顺序
                if (current[i] != 0) {
                    return true;
                }
            }
            break;
        case LINE:
            if (branch >= dimens - 1) {                                                 // 无界
                return false;
            } else if (target[branch] * current[branch] < 0) {                          // 排除反向
                return true;
            }
            for (int i = branch; i < dimens - 1; i++) {                                 // 维度顺序
                if (current[i] != 0) {
                    return true;
                }
            }
            break;
        case SURFACE:
            /**
             * 1 equal branch == dimens
             * return true
             * 2 target[0] == current[0] return true
             * 3 target[0] != current[0] return false
             */
            // return target[0] == current[0];
            return (current[0] - target[0]) * current[0] > 0;
            // if (target[0] != current[0]) { // 抵达目标平面
            // return false;
            // } else if (target[branch] * current[branch] < 0) { // 排除反向
            // return true;
            // }
            // for (int i = branch; i < dimens - 1; i++) { // 维度顺序
            // if (current[branch] != 0) {
            // return true;
            // }
            // }
            // break;
        }
        return false;
    }
}