package com.jsf2184.hackerrank;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PracticeTests {
    @Test
    public void testSimpleArraySum() {
        int res = simpleArraySum(new int[]{1, 2, 3});
        Assert.assertEquals(6, res);
    }

    static int simpleArraySum(int[] ar) {
        int res = Arrays.stream(ar).sum();
        return res;
    }

    @Test
    public void testSolve() {
        int[] res = solve(10, 20, 30, 9, 21, 31);
        Assert.assertArrayEquals(new int[] {1, 2}, res);
    }
    static int[] solve(int a0, int a1, int a2, int b0, int b1, int b2) {
        /*
         * Write your code here.
         */
        int[] res = new int[2];
        compareAndSet(a0, b0, res);
        compareAndSet(a1, b1, res);
        compareAndSet(a2, b2, res);
        return res;

    }

    public static void compareAndSet(int a, int b, int[] res) {
        Integer idx = null;
        if (a > b) {
            idx = 0;
        } else if (b > a) {
            idx = 1;
        }
        if (idx != null) {
            res[idx]++;
        }
    }

    @Test
    public void testVeryBigSum() {
        long res = aVeryBigSum(3, new long[]{1L, 2L, 3L});
        Assert.assertEquals(6L, res);
    }

    static long aVeryBigSum(int n, long[] ar) {
        /*
         * Write your code here.
         */
        long res = Arrays.stream(ar).sum();
        return res;
    }

    @Test
    public void testDiagonalDifference() {
        int[][] input = {
                {11, 2, 4},
                {4, 5, 6},
                {10, 8, -12}
        };
        int res = diagonalDifference(input);
        Assert.assertEquals(15, res);
    }
    static int diagonalDifference(int[][] a) {
        /*
         * Write your code here.
         */
        int len = a.length;
        int diag1Sum = 0;
        for (int i=0; i<len; i++) {
            diag1Sum += a[i][i];
        }
        int diag2Sum = 0;
        for (int i=0; i<len; i++) {
            diag2Sum += a[i][len - 1 -i];
        }
        int res = Math.abs(diag1Sum - diag2Sum);
        return res;

    }

    @Test
    public void testPlusMinus() {
        plusMinus(new int[] {-4, 3, -9, 0, 4, 1});
    }
    static void plusMinus(int[] arr) {
        /*
         * Write your code here.
         */
        int[] counts = new int[3];
        final int pos = 0;
        final int neg = 1;
        final int zero = 2;
        for (int a : arr) {
            int idx;
            if (a > 0) {
                idx = pos;
            } else if (a < 0) {
                idx = neg;
            } else {
                idx = zero;
            }
            counts[idx]++;
        }
        double len = arr.length;
        Arrays.stream(counts).forEach(x -> System.out.printf("%8.6f\n", x/len));
    }

    @Test
    public void printStaircase() {
        staircase2(5);
    }

    public void staircase(int n) {
        for (int r=1; r<=n; r++) {
            for (int c = n; c >= 1; c--) {
                char ch = c > r ? ' ' : '#';
                System.out.print(ch);
            }
            System.out.println("");
        }
    }

    public void staircase2(int n) {
        for (int r=1; r<=n; r++) {
            int numSpaces = n-r;
            for (int c=1; c<= n; c++) {
                char ch = c <= numSpaces? ' ' : '#';
                System.out.print(ch);
            }
            System.out.println("");
        }
    }

}
