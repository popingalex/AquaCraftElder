package org.aqua.graph.pixel;

import java.util.LinkedList;

import javax.swing.event.ListSelectionEvent;

public class Pixel {
    private Integer                                     fieldLength;
    private Integer                                     width;      // x
    private Integer                                     length;     // y
    private Integer                                     height;     // z
    private LinkedList<LinkedList<LinkedList<Integer>>> content;

    public boolean checkLegal(Integer x, Integer y, Integer z) {
        return content.size() > z && content.get(z).size() > y && content.get(z).get(y).size() > x;
    }

    public void setPoint(Integer x, Integer y, Integer z, Integer value) {
        content.get(z).get(y).set(x, value);
    }

    public void clearPoint(Integer x, Integer y, Integer z) {
        setPoint(x, y, z, 0);
    }

    public void clearLine(Integer x, Integer y, Integer z) {
        if (x < 0 && y >= 0 && z >= 0) {
            checkLegal(0, y, z);
            for (int i = 0, sum = content.get(0).get(0).size(); i < sum; i++) {
                clearPoint(i, y, z);
            }
        } else if (y < 0 && x >= 0 && z >= 0) {
            checkLegal(x, 0, z);
            for (int i = 0, sum = content.get(0).size(); i < sum; i++) {
                clearPoint(x, i, z);
            }
        } else if (z < 0 && x >= 0 && y >= 0) {
            checkLegal(x, y, 0);
            for (int i = 0, sum = content.size(); i < sum; i++) {
                clearPoint(x, y, i);
            }
        }
    }
    public void clearPlane(Integer x, Integer y, Integer z) {

    }
}
