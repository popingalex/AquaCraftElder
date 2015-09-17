package org.aqua.structure.space;

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
        int[] current = Util.shift(coords, dimens - dimen % dimens);
        int[] target = Util.shift(node.coords, dimens - dimen % dimens);
        int branch = Util.indexBranch(current, target, 0);
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
    public boolean stop(SpaceNode node) {
        int[] current = Util.shift(coords, dimens - dimen % dimens);
        int[] target = Util.shift(node.coords, dimens - dimen % dimens);
        int branch = Util.indexBranch(current, target, 0);

        switch (mode) {
        case POINT:
            if (branch == dimens) {                                                     // 香蕉
                return true;
            } else if (target[branch] * current[branch] < 0) {                          // 排除反向
                return true;
            }
            for (int i = branch; i < dimens; i++) {                                     // 维度顺序
                if (target[branch] != 0) {
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
                if (target[branch] != 0) {
                    return true;
                }
            }
            break;
        case SURFACE:
            if (target[dimens - 1] == current[dimens - 1]) {                            // 无界
                return false;
            } else if (target[branch] * current[branch] < 0) {                          // 排除反向
                return true;
            }
            for (int i = branch; i < dimens - 1; i++) {                                 // 维度顺序
                if (target[branch] != 0) {
                    return true;
                }
            }
            break;
        }
        return false;
    }
}