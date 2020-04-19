package codility_lessons;

import java.util.Arrays;

/**
 * Compute the number of intersections in a sequence of discs
 */
// https://codility.com/programmers/lessons/6-sorting/number_of_disc_intersections/
public class NumberOfDiscIntersections {

    static class Circle implements Comparable<Circle> {
        public Circle(int center, int radius) {
            this.center = center;
            this.radius = radius;
            this.leftmost = center - radius;
            this.rightmost = radius + center;
        }
        int leftmost;
        int rightmost;
        int center;
        int radius;
        @Override
        public int compareTo(Circle o) {
            return this.leftmost - o.leftmost;
        }
        
    }
    public int solve(int[] radiuses) {
        Circle[] circles = new Circle[radiuses.length];
        for (int i = 0; i < circles.length; i++) {
            circles[i] = new Circle(i, radiuses[i]);
        }
        Arrays.sort(circles);
        int intersections = 0;
        for (int i = 0; i < circles.length - 1; i++) {
            for (int j = i + 1; j < circles.length && circles[i].rightmost >= circles[j].leftmost; j++) {
                if (++intersections > 10000000) {
                    return -1;
                }
            }
        }
        return intersections;
    }

}
