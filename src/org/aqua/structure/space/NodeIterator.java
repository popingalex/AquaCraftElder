package org.aqua.structure.space;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

public class NodeIterator implements Comparator<SpaceNode> {
    protected Logger logger = Logger.getLogger(getClass());
    private List<SpaceNode> nodelist = new LinkedList<SpaceNode>();

    public final List<SpaceNode> getNodelist() {
        return getNodelist(false);
    }

    public final List<SpaceNode> getNodelist(boolean sort) {
        if (sort) {
            Collections.sort(nodelist, this);
        }
        return nodelist;
    }

    public boolean accept(SpaceNode node) {
        return true;
    }

    public boolean interrupt(SpaceNode node) {
        return false;
    }

    public void iterate(SpaceNode node) {
    }

    public int compare(SpaceNode n1, SpaceNode n2) {
        for (int i = 0, dimens = n1.coords.length; i < dimens; i++) {
            if (n1.coords[i] != n2.coords[i]) {
                return n1.coords[i] - n2.coords[i];
            }
        }
        return 0;
    }
}