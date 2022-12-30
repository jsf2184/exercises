package com.jsf2184.cracking;

import org.junit.Test;

public class RotateImamge_1_7 {


    int n;


    public void rotate(int[][] image) {
        int n = image.length;
        for (int origin=0; n > 1; n-=2, origin++) {
            Frame frame = new Frame(origin, image, n);
            frame.rotate();
        }
    }

    public static class Point {
        public int x;
        public int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Point(Point src) {
            x = src.x;
            y = src.y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Point point = (Point) o;

            if (x != point.x) return false;
            return y == point.y;
        }
    }
    
    public static class Frame {
        private final int origin;
        private final int[][] image;
        private final int n;
        private final int max;

        public Frame(int origin, int[][] image, int n) {
            this.origin = origin;
            this.image = image;
            this.n = n;
            max = n-1;
            System.out.printf("Working on Frame of size: %d with origin: %d\n", n, origin);
        }

        public void rotate() {
            // to do this in place, we operate on 4 elements at a time of the frame
            for (int destX=0; destX <max; destX++) {
                Point start = new Point(destX, 0);
                int save_pixel = valueAt(start);
                Point dest = start;
                Point src = null;

                while (true) {
                    src = getSource(dest);
                    if (src.equals(start)) {
                        break;
                    }
                    int pixel = valueAt(src);
                    setValue(pixel, dest);
                    dest = src;
                }
                setValue(save_pixel, dest);
            }
        }


        private Point getSource(Point dest) {
            int srcX, srcY;
            if (dest.y == 0)  {          // dest is top row
                srcX  = 0;
                srcY = max - dest.x;
            } else if (dest.y == max) {  // dest is bottom row
                srcX = max;
                srcY = max - dest.x;
            } else if (dest.x == 0) {    // dest is left edge
                srcX = dest.y;
                srcY = max;
            } else {                     // dest is right edge
                srcX = dest.y;
                srcY = 0;
            }
            return new Point(srcX, srcY);
        }

        private int valueAt(Point p) {
            Point rp = realPoint(p);
            return  image[rp.y][rp.x];
        }

        private void setValue(int pixel, Point dest) {
            Point rp = realPoint(dest);
            image[rp.y][rp.x] = pixel;
        }

        Point realPoint(Point relativePoint) {
            return new Point(relativePoint.x + origin, relativePoint.y + origin);
        }
    }

    @Test
    public void runIt() {
        final int[][] image = createImage(4);
        printImage(image);
        rotate(image);
        printImage(image);

    }

    public static int[][]  createImage(int n) {
        int[][] image = new int[n][];
        for (int i=0, v=0; i<n; i++) {
            image[i] = new int[n];
            for (int j=0; j < n; j++, v++) {
                image[i][j] = v;
            }
        }
        return image;
    }

    public static void printImage(int[][] image) {
        for (int[] row : image) {
            for(int pixel : row) {
                System.out.printf(" %02d", pixel);
            }
            System.out.println();
        }
    }
}
