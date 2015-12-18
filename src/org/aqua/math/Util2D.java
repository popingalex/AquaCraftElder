package org.aqua.math;

public class Util2D {
    public static double cross(double[] p1, double[] p2, double[] p3, double[] p4) {
        return (p2[0] - p1[0]) * (p2[1] - p1[1]) - (p4[0] - p3[0]) * (p4[1] - p3[1]);
    }
}
