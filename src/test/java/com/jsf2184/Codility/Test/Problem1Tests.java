package com.jsf2184.Codility.Test;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

public class Problem1Tests {

    int[] sampleData9 = {1, 3, 2, 1, 2, 1, 5, 3, 3, 4, 2};
    int[] sampleData8 = {5, 8};
    int[] sampleData1 = {1, 1, 1, 1};
    int[] sampleData10 = {10};

    public int bruteForce(int[] A) {
        long total = 0;

        for (int r=1; true; r++) {
            int rowStrokes = processRow(r, A);
            if (rowStrokes == 0) {
                // r is past the highest height so we are done.
                break;
            }
            // Our total is the sum of the brushstrokes on every row.
            total += rowStrokes;
            if (total > 1000000000) {
                return -1;
            }
        }
        return (int) total;
    }

    int solution(int[] A) {
        long totalBrushstrokes = 0;
        final int billion = 1000000000;
        int prev = 0;
        for (int a : A) {
            if (a > prev) {
                // every time we grow taller, we have to add the delta to our totalBrushstrokes
                long delta = a - prev;
                totalBrushstrokes += delta;
                if (totalBrushstrokes > billion) {
                    return -1;
                }
            }
            prev = a;
        }
        return (int) totalBrushstrokes;
    }

    // count the number of brushstrokes needed for this row.
    public static int processRow(int r, int[] A) {
        int brushStrokes = 0;
        boolean lastIsOne = false;
        for (int a : A) {
            if (r <= a) {
                // this column is high enough to handle the row. If lastIsOne is true, its predecessor was also.
                if (!lastIsOne) {
                    // It was not, so we need to add another brushstroke.
                    // In other words, transitions from not there to there, require a new brushstroke
                    brushStrokes++;
                    lastIsOne = true;
                }
            } else {
                lastIsOne = false;
            }
        }
        return brushStrokes;
    }

    @Test
    public void testSolution() {
        Assert.assertEquals(9, solution(sampleData9));
        Assert.assertEquals(8, solution(sampleData8));
        Assert.assertEquals(1, solution(sampleData1));
        Assert.assertEquals(10, solution(sampleData10));
    }

    @Test
    public void testSmallRandomSolutions() {
        validateRandomSolutions(5, 20, 10, true);
    }

    @Test
    public void testLargeRandomSolutions() {
        validateRandomSolutions(10, 30000, 100000, false);
    }


    public void validateRandomSolutions(int tries, int len, int maxHeight, boolean printArray) {
        for (int i=0; i<tries; i++) {
            int[] A;
            if (printArray) {
                A = generateAndPrintArray(len, maxHeight);
            } else {
                A = generateArray(len, maxHeight);
            }
            int optimizedRes = solution(A);
            int bruteForceRes = bruteForce(A);
            System.out.printf("optimizedRes = %d, bruteForceRes = %d\n", optimizedRes, bruteForceRes);
            Assert.assertEquals(optimizedRes, bruteForceRes);
        }
    }
    @Test
    public void testProcessRow() {
        Assert.assertEquals(1, processRow(1, sampleData9));
        Assert.assertEquals(3, processRow(2, sampleData9));
        Assert.assertEquals(2, processRow(3, sampleData9));
        Assert.assertEquals(2, processRow(4, sampleData9));
        Assert.assertEquals(1, processRow(5, sampleData9));
        Assert.assertEquals(0, processRow(6, sampleData9));
    }

    @Test
    public void testGenerateArray() {
        generateAndPrintArray(20, 5);
    }


    private static int[] generateAndPrintArray(int len, int maxHeight) {
        System.out.printf("maxHeight = %d\n", maxHeight);
        int[] res = generateArray(len, maxHeight);
        System.out.println(Arrays.toString(res));
        return res;
    }


    private static int[] generateArray(int len, int maxHeight) {
        int[] res = new int[len];
        Random r = new Random();
        for (int i=0; i< len; i++) {
            int val = r.nextInt(maxHeight) + 1;
            res[i] = val;
        }
        return res;
    }


//    @Test
//    public void testMax() {
//        Assert.assertEquals(7, findMax( new int[] {2, 3, 4, 7, 1}));
//        Assert.assertEquals(1, findMax( new int[] {1, 1}));
//    }

//    int findMax(int[] A) {
//        int res = 0;
//        for (int i=0; i<A.length; i++) {
//            if (A[i] > res) {
//                res = A[i];
//            }
//        }
//        return res;
//    }

}
