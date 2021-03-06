import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;

/*
 * Copyright (C) 2016 Michael <GrubenM@GMail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 *
 * @author Michael <GrubenM@GMail.com>
 */
public class BruteCollinearPoints {
    private LineSegment[] segments;
    private int size;
    private Point[] pts; // mutates for each new "origin"

    /**
     * Finds all line segments containing 4 points
     * @param points
     */
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new java.lang.NullPointerException();
        pts = new Point[points.length];
        for (int i = 0; i < points.length; i++) pts[i] = points[i];

        /**
         * Checking for null points goes way faster if we've already sorted
         * the points array, but the API requires that we can't mutate the
         * incoming points.  Hence we make pts, and sort that.
         */
        Arrays.sort(pts);
        for (int i = 0; i < pts.length - 1; i++) {
            if (pts[i] == null) throw new java.lang.NullPointerException();
            if (pts[i].compareTo(pts[i + 1]) == 0)
                throw new java.lang.IllegalArgumentException();
        }

        // Store the segments we've found in a ResizingArray
        segments = new LineSegment[1];
        size = 0;

        /**
         * "For simplicity, we will not supply any input to BruteCollinearPoints
         * that has 5 or more collinear points."
         * <http://coursera.cs.princeton.edu/algs4/assignments/collinear.html>
         *
         * Accordingly, consider all possible combinations of 4 Points,
         * sort them to determine which Points are the endpoints, and
         * no additional checks are needed.
         *
         * (e.g., since we won't see 5 collinear Points, we don't have to worry
         * about later seeing only 4 of them and checking if this is a new
         * LineSegment.  Also, we don't have the A-B B-A symmetry problem here,
         * since we're only considering each potential line segment once, and
         * so we don't have the chance to adopt a different perspective.)
         */
        Point[] subset = new Point[4];
        for (int i = 0 + 0; i < pts.length - 3; i++) {
            subset[0] = pts[i];
            for (int j = i + 1; j < pts.length - 2; j++) {
                subset[1] = pts[j];
                for (int k = j + 1; k < pts.length - 1; k++) {
                    subset[2] = pts[k];
                    for (int l = k + 1; l < pts.length - 0; l++) {
                        subset[3] = pts[l];
                        Arrays.sort(subset);
                        double slopeA = subset[0].slopeTo(subset[1]);
                        double slopeB = subset[0].slopeTo(subset[2]);
                        double slopeC = subset[0].slopeTo(subset[3]);
                        if (slopeA == slopeB && slopeB == slopeC) {
                            enqueue(new LineSegment(subset[0], subset[3]));
                        }
                    }
                }
            }
        }
    }

    /**
     * "Add the item"
     * "Throw a java.lang.NullPointerException if the client attempts to add a
     * null item"
     *
     * Also doubles the length of the array when it is full.
     *
     * Adapted from the RandomizedQueue assignment.
     */
    private void enqueue(LineSegment l)
    {
        if (l == null) throw new java.lang.NullPointerException();
        if (size == segments.length)
            resize(2 * segments.length, segments);
        segments[size++] = l;
    }

    /**
     * Resizes the array segments to [capacity].
     *
     * This is a quadratic operation in the length of segments,
     * and so should only be performed sparingly.
     *
     * Amortizing this cost over the number of operations which
     * can be performed in the new array, however,
     * the ResizingArray is constant.
     *
     * Adapted from the RandomizedQueue assignment.
     */
    private void resize(int capacity, LineSegment[] l) {
        LineSegment[] copy = new LineSegment[capacity];
        for (int i = 0; i < size; i++) copy[i] = l[i];
        segments = copy;
    }

    /**
     * Returns the number of line segments.
     * @return
     */
    public int numberOfSegments() {
        return size;
    }

    /**
     * Returns a copy of the line segments in an array with no null elements.
     * @return
     */
    public LineSegment[] segments() {
        LineSegment[] shrunk = new LineSegment[size];
        for (int i = 0; i < size; i++) shrunk[i] = segments[i];
        return shrunk;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In("collinear/input48.txt");
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}