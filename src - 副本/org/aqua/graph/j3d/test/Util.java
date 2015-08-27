package org.aqua.graph.j3d.test;

import javax.media.j3d.PickSegment;
import javax.vecmath.Point3d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

public class Util {
    public static boolean segmentAndPoly(Point3d coordinates[], PickSegment segment, double dist[]) {

        Vector3d vec0 = new Vector3d(); // Edge vector from point 0 to point 1;
        Vector3d vec1 = new Vector3d(); // Edge vector from point 0 to point 2
                                        // or 3;
        Vector3d pNrm = new Vector3d();
        double absNrmX, absNrmY, absNrmZ, pD = 0.0;
        Vector3d tempV3d = new Vector3d();
        Vector3d direction = new Vector3d();
        double pNrmDotrDir = 0.0;
        int axis, nc, sh, nsh;
        Point3d start = new Point3d();
        Point3d end = new Point3d();

        Point3d iPnt = new Point3d(); // Point of intersection.

        double uCoor[] = new double[4]; // Only need to support up to quad.
        double vCoor[] = new double[4];
        double tempD;

        int i, j;

        // Compute plane normal.
        for (i = 0; i < coordinates.length - 1;) {
            vec0.x = coordinates[i + 1].x - coordinates[i].x;
            vec0.y = coordinates[i + 1].y - coordinates[i].y;
            vec0.z = coordinates[i + 1].z - coordinates[i++].z;
            if (vec0.length() > 0.0)
                break;
        }

        for (j = i; j < coordinates.length - 1; j++) {
            vec1.x = coordinates[j + 1].x - coordinates[j].x;
            vec1.y = coordinates[j + 1].y - coordinates[j].y;
            vec1.z = coordinates[j + 1].z - coordinates[j].z;
            if (vec1.length() > 0.0)
                break;
        }

        if (j == (coordinates.length - 1)) {
            // System.out.println("(1) Degenerated polygon.");
            return false; // Degenerated polygon.
        }

        /*
         * System.out.println("Triangle/Quad :"); for(i=0; i<coordinates.length;
         * i++) System.out.println("P" + i + " " + coordinates[i]);
         */

        pNrm.cross(vec0, vec1);

        if (pNrm.length() == 0.0) {
            // System.out.println("(2) Degenerated polygon.");
            return false; // Degenerated polygon.
        }
        // Compute plane D.
        tempV3d.set((Tuple3d) coordinates[0]);
        pD = pNrm.dot(tempV3d);

        segment.get(start, end);
        // System.out.println("Segment start : " + start + " end " + end);

        direction.x = end.x - start.x;
        direction.y = end.y - start.y;
        direction.z = end.z - start.z;

        pNrmDotrDir = pNrm.dot(direction);

        // Segment is parallel to plane.
        if (pNrmDotrDir == 0.0) {
            // System.out.println("Segment is parallel to plane.");
            return false;
        }

        tempV3d.set((Tuple3d) start);

        dist[0] = (pD - pNrm.dot(tempV3d)) / pNrmDotrDir;

        // Segment intersects the plane behind the segment's start.
        // or exceed the segment's length.
        if ((dist[0] < 0.0) || (dist[0] > 1.0)) {
            // System.out.println("Segment intersects the plane behind the start or exceed end.");
            return false;
        }

        // Now, one thing for sure the segment intersect the plane.
        // Find the intersection point.
        iPnt.x = start.x + direction.x * dist[0];
        iPnt.y = start.y + direction.y * dist[0];
        iPnt.z = start.z + direction.z * dist[0];

        // System.out.println("dist " + dist[0] + " iPnt : " + iPnt);

        // Project 3d points onto 2d plane and apply Jordan curve theorem.
        // Note : Area of polygon is not preserve in this projection, but
        // it doesn't matter here.

        // Find the axis of projection.
        absNrmX = Math.abs(pNrm.x);
        absNrmY = Math.abs(pNrm.y);
        absNrmZ = Math.abs(pNrm.z);

        if (absNrmX > absNrmY)
            axis = 0;
        else
            axis = 1;

        if (axis == 0) {
            if (absNrmX < absNrmZ)
                axis = 2;
        } else if (axis == 1) {
            if (absNrmY < absNrmZ)
                axis = 2;
        }

        // System.out.println("Normal " + pNrm + " axis " + axis );

        for (i = 0; i < coordinates.length; i++) {
            switch (axis) {
            case 0:
                uCoor[i] = coordinates[i].y - iPnt.y;
                vCoor[i] = coordinates[i].z - iPnt.z;
                break;

            case 1:
                uCoor[i] = coordinates[i].x - iPnt.x;
                vCoor[i] = coordinates[i].z - iPnt.z;
                break;

            case 2:
                uCoor[i] = coordinates[i].x - iPnt.x;
                vCoor[i] = coordinates[i].y - iPnt.y;
                break;
            }

            // System.out.println("i " + i + " u " + uCoor[i] + " v " +
            // vCoor[i]);
        }

        // initialize number of crossing, nc.
        nc = 0;

        if (vCoor[0] < 0.0)
            sh = -1;
        else
            sh = 1;

        for (i = 0; i < coordinates.length; i++) {
            j = i + 1;
            if (j == coordinates.length)
                j = 0;

            if (vCoor[j] < 0.0)
                nsh = -1;
            else
                nsh = 1;

            if (sh != nsh) {
                if ((uCoor[i] > 0.0) && (uCoor[j] > 0.0)) {
                    // This line must cross U+.
                    nc++;
                } else if ((uCoor[i] > 0.0) || (uCoor[j] > 0.0)) {
                    // This line might cross U+. We need to compute intersection
                    // on U azis.
                    tempD = uCoor[i] - vCoor[i] * (uCoor[j] - uCoor[i]) / (vCoor[j] - vCoor[i]);
                    if (tempD > 0)
                        // This line cross U+.
                        nc++;
                }
                sh = nsh;
            } // sh != nsh
        }

        // System.out.println("nc " + nc);

        if ((nc % 2) == 1) {

            // calculate the distance
            dist[0] *= direction.length();

            // System.out.println("Segment Intersected!");
            /*
             * System.out.println("Segment orgin : " + start + " dir " +
             * direction); System.out.println("Triangle/Quad :"); for(i=0;
             * i<coordinates.length; i++) System.out.println("P" + i + " " +
             * coordinates[i]); System.out.println("dist " + dist[0] +
             * " iPnt : " + iPnt);
             */
            return true;
        } else {
            // System.out.println("Segment Not Intersected!");
            return false;
        }
    }

    public static boolean bat(Point3d loc, double w, double l, double h, float[] coords, int[] stripCounts, int nStrips) {
        Point3d[][] points = new Point3d[][] {
                new Point3d[] { new Point3d(loc.x - w / 2, loc.y - l / 2, loc.z - h / 2),
                        new Point3d(loc.x - w / 2, loc.y - l / 2, loc.z + h / 2) },
                new Point3d[] { new Point3d(loc.x - w / 2, loc.y + l / 2, loc.z - h / 2),
                        new Point3d(loc.x - w / 2, loc.y + l / 2, loc.z + h / 2) },
                new Point3d[] { new Point3d(loc.x + w / 2, loc.y - l / 2, loc.z - h / 2),
                        new Point3d(loc.x + w / 2, loc.y - l / 2, loc.z + h / 2) },
                new Point3d[] { new Point3d(loc.x + w / 2, loc.y + l / 2, loc.z - h / 2),
                        new Point3d(loc.x + w / 2, loc.y + l / 2, loc.z + h / 2) },

                new Point3d[] { new Point3d(loc.x - w / 2, loc.y - l / 2, loc.z - h / 2),
                        new Point3d(loc.x - w / 2, loc.y + l / 2, loc.z - h / 2) },
                new Point3d[] { new Point3d(loc.x - w / 2, loc.y - l / 2, loc.z + h / 2),
                        new Point3d(loc.x - w / 2, loc.y + l / 2, loc.z + h / 2) },
                new Point3d[] { new Point3d(loc.x + w / 2, loc.y - l / 2, loc.z - h / 2),
                        new Point3d(loc.x + w / 2, loc.y + l / 2, loc.z - h / 2) },
                new Point3d[] { new Point3d(loc.x + w / 2, loc.y - l / 2, loc.z + h / 2),
                        new Point3d(loc.x + w / 2, loc.y + l / 2, loc.z + h / 2) },

                new Point3d[] { new Point3d(loc.x - w / 2, loc.y - l / 2, loc.z - h / 2),
                        new Point3d(loc.x + w / 2, loc.y - l / 2, loc.z - h / 2) },
                new Point3d[] { new Point3d(loc.x - w / 2, loc.y - l / 2, loc.z + h / 2),
                        new Point3d(loc.x + w / 2, loc.y - l / 2, loc.z + h / 2) },
                new Point3d[] { new Point3d(loc.x - w / 2, loc.y + l / 2, loc.z - h / 2),
                        new Point3d(loc.x + w / 2, loc.y + l / 2, loc.z - h / 2) },
                new Point3d[] { new Point3d(loc.x - w / 2, loc.y + l / 2, loc.z + h / 2),
                        new Point3d(loc.x + w / 2, loc.y + l / 2, loc.z + h / 2) }, };
        for (Point3d[] seg : points) {
            // if (segmentAndPoly(trips, new PickSegment(segment[0],
            // segment[1]), dist)) {
            // return true;
            // }
            Vector3d v = new Vector3d(seg[1].x - seg[0].x, seg[1].y - seg[0].y, seg[1].z - seg[0].z);
            if (rayTriangle(seg[0], v, seg[0].distance(seg[1]), coords, stripCounts, nStrips, new Point3d(), true)) {
                return true;
            }

        }
        return false;
    }

    public static boolean rayTriangle(Point3d origin, Vector3d direction, double length, float[] coords,
            int[] stripCounts, int numStrips, Point3d point, boolean intersectOnly) {
        if (numStrips == 0)
            return false;

        // Add all the strip lengths up first
        int total_coords = 0;

        for (int i = numStrips; --i >= 0;)
            total_coords += stripCounts[i];

        if (coords.length < total_coords * 3)
            throw new IllegalArgumentException("coords too small for numCoords");

        // assign the working coords to be big enough for a quadrilateral as
        // that is what we are most likely to see as the biggest item
        float[] wkPolygon = new float[12];
        Vector3d diffVec = new Vector3d();
        Point3d wkPoint = new Point3d();

        double shortest_length = Double.POSITIVE_INFINITY;
        boolean found = false;
        double this_length;
        int offset = 0;

        for (int i = 0; i < numStrips; i++) {
            for (int j = 0; j < stripCounts[i] - 2; j++) {
                System.arraycopy(coords, offset, wkPolygon, 0, 9);

                if (rayPolygonChecked(origin, direction, length, wkPolygon, 3, wkPoint)) {
                    found = true;
                    diffVec.sub(origin, wkPoint);

                    this_length = diffVec.lengthSquared();

                    if (this_length < shortest_length) {
                        shortest_length = this_length;
                        point.set(wkPoint);

                        if (intersectOnly)
                            break;
                    }
                }

                offset += 3;
            }

            // shift the offset onto the start of the next strip.
            offset += 6;
        }

        return found;
    }

    private static boolean rayPolygonChecked(Point3d origin, Vector3d direction, double length, float[] coords,
            int numCoords, Point3d point) {
        int i, j;
        Vector3d v0 = new Vector3d();
        Vector3d v1 = new Vector3d();
        Vector3d normal = new Vector3d();
        Vector3d wkVec = new Vector3d();
        float[] working2dCoords = new float[8];

        v0.x = coords[3] - coords[0];
        v0.y = coords[4] - coords[1];
        v0.z = coords[5] - coords[2];

        v1.x = coords[6] - coords[3];
        v1.y = coords[7] - coords[4];
        v1.z = coords[8] - coords[5];

        normal.cross(v0, v1);

        // degenerate polygon?
        if (normal.lengthSquared() == 0)
            return false;

        double n_dot_dir = normal.dot(direction);

        // ray and plane parallel?
        if (n_dot_dir == 0)
            return false;

        wkVec.x = coords[0];
        wkVec.y = coords[1];
        wkVec.z = coords[2];
        double d = normal.dot(wkVec);

        wkVec.set(origin);
        double n_dot_o = normal.dot(wkVec);

        // t = (d - N.O) / N.D
        double t = (d - n_dot_o) / n_dot_dir;

        // intersection before the origin
        if (t < 0)
            return false;

        // So we have an intersection with the plane of the polygon and the
        // segment/ray. Using the winding rule to see if inside or outside
        // First store the exact intersection point anyway, regardless of
        // whether this is an intersection or not.
        point.x = origin.x + direction.x * t;
        point.y = origin.y + direction.y * t;
        point.z = origin.z + direction.z * t;

        // Intersection point after the end of the segment?
        if ((length != 0) && (origin.distance(point) > length))
            return false;

        // bounds check

        // find the dominant axis to resolve to a 2 axis system
        double abs_nrm_x = (normal.x >= 0) ? normal.x : -normal.x;
        double abs_nrm_y = (normal.y >= 0) ? normal.y : -normal.y;
        double abs_nrm_z = (normal.z >= 0) ? normal.z : -normal.z;

        int dom_axis;

        if (abs_nrm_x > abs_nrm_y)
            dom_axis = 0;
        else
            dom_axis = 1;

        if (dom_axis == 0) {
            if (abs_nrm_x < abs_nrm_z)
                dom_axis = 2;
        } else if (abs_nrm_y < abs_nrm_z) {
            dom_axis = 2;
        }

        // Map all the coordinates to the 2D plane. The u and v coordinates
        // are interleaved as u == even indicies and v = odd indicies

        // Steps 1 & 2 combined
        // 1. For NV vertices [Xn Yn Zn] where n = 0..Nv-1, project polygon
        // vertices [Xn Yn Zn] onto dominant coordinate plane (Un Vn).
        // 2. Translate (U, V) polygon so intersection point is origin from
        // (Un', Vn').
        j = 2 * numCoords - 1;

        switch (dom_axis) {
        case 0:
            for (i = numCoords; --i >= 0;) {
                working2dCoords[j--] = coords[i * 3 + 2] - (float) point.z;
                working2dCoords[j--] = coords[i * 3 + 1] - (float) point.y;
            }
            break;

        case 1:
            for (i = numCoords; --i >= 0;) {
                working2dCoords[j--] = coords[i * 3 + 2] - (float) point.z;
                working2dCoords[j--] = coords[i * 3] - (float) point.x;
            }
            break;

        case 2:
            for (i = numCoords; --i >= 0;) {
                working2dCoords[j--] = coords[i * 3 + 1] - (float) point.y;
                working2dCoords[j--] = coords[i * 3] - (float) point.x;
            }
            break;
        }

        int sh; // current sign holder
        int nsh; // next sign holder
        float dist;
        int crossings = 0;

        // Step 4.
        // Set sign holder as f(V' o) ( V' value of 1st vertex of 1st edge)
        if (working2dCoords[1] < 0.0)
            sh = -1;
        else
            sh = 1;

        for (i = 0; i < numCoords; i++) {
            // Step 5.
            // For each edge of polygon (Ua' V a') -> (Ub', Vb') where
            // a = 0..Nv-1 and b = (a + 1) mod Nv

            // b = (a + 1) mod Nv
            j = (i + 1) % numCoords;

            int i_u = i * 2; // index of Ua'
            int j_u = j * 2; // index of Ub'
            int i_v = i * 2 + 1; // index of Va'
            int j_v = j * 2 + 1; // index of Vb'

            // Set next sign holder (Nsh) as f(Vb')
            // Nsh = -1 if Vb' < 0
            // Nsh = +1 if Vb' >= 0
            if (working2dCoords[j_v] < 0.0)
                nsh = -1;
            else
                nsh = 1;

            // If Sh <> NSH then if = then edge doesn't cross +U axis so no
            // ray intersection and ignore

            if (sh != nsh) {
                // if Ua' > 0 and Ub' > 0 then edge crosses + U' so Nc = Nc + 1
                if ((working2dCoords[i_u] > 0.0) && (working2dCoords[j_u] > 0.0)) {
                    crossings++;
                } else if ((working2dCoords[i_u] > 0.0) || (working2dCoords[j_u] > 0.0)) {
                    // if either Ua' or U b' > 0 then line might cross +U, so
                    // compute intersection of edge with U' axis
                    dist = working2dCoords[i_u]
                            - (working2dCoords[i_v] * (working2dCoords[j_u] - working2dCoords[i_u]))
                            / (working2dCoords[j_v] - working2dCoords[i_v]);

                    // if intersection point is > 0 then must cross,
                    // so Nc = Nc + 1
                    if (dist > 0)
                        crossings++;
                }

                // Set SH = Nsh and process the next edge
                sh = nsh;
            }
        }

        // Step 6. If Nc odd, point inside else point outside.
        // Note that we have already stored the intersection point way back up
        // the start.
        return ((crossings % 2) == 1);
    }
}
