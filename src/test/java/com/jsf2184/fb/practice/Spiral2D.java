package com.jsf2184.fb.practice;

import org.junit.Test;

import java.util.Arrays;
import java.util.function.Function;

// https://www.facebook.com/careers/life/sample_interview_questions
public class Spiral2D {

    public static class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    enum Direction {
        Right(p -> new Point(p.x+1, p.y)),
        Down(p-> new Point(p.x, p.y+1)),
        Left(p->new Point(p.x-1, p.y)),
        Up(p->new Point(p.x, p.y-1));

        Direction next;

        Direction(Function<Point, Point> nextPoint) {
            this.nextPoint = nextPoint;
        }

        Point getNextPoint(Point p) {
            final Point result = nextPoint.apply(p);
            return result;
        }

        Function<Point, Point> nextPoint;

    }

    public static class Square {
        public Point origin;
        public int length;
        int startingValue;

        public Square(Point origin, int length, int startingValue) {
            this.origin = origin;
            this.length = length;
            this.startingValue = startingValue;
        }

        public Square populate(int[][] array ) {
            Point p = origin;
            int value = startingValue;
            for (Direction d : Direction.values()) {
                for (int i=0; i<length-1; i++) {
                    array[p.y][p.x] = value;
                    value++;
                    p = d.getNextPoint(p);
                }
            }
            return nextSquare(value);
        }
        public Square nextSquare(int startingValue)  {
            int nextLen = length - 2;
            if (nextLen < 1) {
                return null;
            }
            Point nextOrigin = new Point(origin.x+1, origin.y + 1);
            Square result = new Square(nextOrigin, nextLen, startingValue);
            return result;
        }



    }
    public static int[][] spiral(int n) {
        // Allocate our result array.
        int[][] res = new int[n][];
        for (int i=0;i<n; i++)  {
            res[i] = new int[n];
        }
        Square square = new Square(new Point(0, 0), n, 1);
        while (square != null) {
            square = square.populate(res);
        }
        return res;
    }

    @Test
    public void testSpiral() {
        final int[][] result = spiral(8);
        printArray(result);

    }

    public static void printArray(int[][] array) {
        for(int r=0; r< array.length; r++) {
            final int[] row = array[r];
            for(int c=0; c<row.length; c++) {
                System.out.printf("%02d ", row[c]);
            }
            System.out.printf("\n");
        }
    }
}
