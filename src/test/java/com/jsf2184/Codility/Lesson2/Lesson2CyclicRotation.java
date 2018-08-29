package com.jsf2184.Codility.Lesson2;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class Lesson2CyclicRotation {

    // In-place solution
    public int[] solution2(int[] A, int K) {
        int len = A.length;

        if (len <= 1) {
            return A;
        }

        int k = K % len;

        for (int i=0; i<k; i++) {
            rotate1(A);
        }
        return A;
    }

    // copying solution
    public int[] solution(int[] A, int K) {
        int len = A.length;

        if (len < 1) {
            return A;
        }

        int k = K % len;
        if (k == 0) {
            return A;
        }

        int[] res = new int[len];
        for (int src = 0; src < len; src++) {
            int dest = (src + k) % len;
            res[dest] = A[src];
        }
        return res;
    }



    @Test
    public void testRotate1() {
        callRotate1(new int[] {0, 1, 2, 3, 4, 5},
                    new int[] {5, 0, 1, 2, 3, 4});
    }

    @Test
    public void testRotate1WithEmpty() {
        callRotate1(new int[] {},
                    new int[] {});

    }

    @Test
    public void testRotate1WithOneElement() {
        callRotate1(new int[] {1},
                    new int[] {1});
    }

    @Test
    public void testRotate1WithTwoElements() {
        callRotate1(new int[] {1,2},
                    new int[] {2,1});
    }
    @Test
    public void testRotate1A() {
        callRotate1A(new int[] {0, 1, 2, 3, 4, 5},
                     new int[] {5, 0, 1, 2, 3, 4});
    }

    @Test
    public void testRotate1AWithEmpty() {
        callRotate1A(new int[] {},
                    new int[] {});

    }

    @Test
    public void testRotate1AWithOneElement() {
        callRotate1A(new int[] {1},
                    new int[] {1});
    }

    @Test
    public void testRotate1AWithTwoElements() {
        callRotate1A(new int[] {1,2},
                    new int[] {2,1});
    }



    @Test
    public void testSolution() {
        int[] res = solution(new int[] {0, 1, 2, 3, 4, 5}, 3);
        Assert.assertTrue(Arrays.equals(new int[] {3, 4, 5, 0, 1, 2 }, res));
    }

    @Test
    public void testSolutionWithEmpty() {
        int[] res = solution(new int[] {}, 3);
        Assert.assertTrue(Arrays.equals(new int[] {}, res));
    }

    @Test
    public void testSolutionWithOneElement() {
        int[] res = solution(new int[] {1}, 3);
        Assert.assertTrue(Arrays.equals(new int[] {1}, res));
    }

    @Test
    public void testSolutionWithTwoElementsEvenNumberOfTimes() {
        int[] res = solution(new int[] {1, 2}, 4);
        Assert.assertTrue(Arrays.equals(new int[] {1, 2}, res));
    }

    @Test
    public void testSolutionWithTwoElementsOddNumberOfTimes() {
        int[] res = solution(new int[] {1, 2}, 1001);
        Assert.assertTrue(Arrays.equals(new int[] {2, 1}, res));
    }

    private void callRotate1(int[] input, int[] expected) {
        System.out.printf("input: %s\n", Arrays.toString(input));
        rotate1(input);
        System.out.printf("output: %s\n", Arrays.toString(input));
        Assert.assertTrue(Arrays.equals(expected, input));
    }

    private void callRotate1A(int[] input, int[] expected) {
        System.out.printf("input: %s\n", Arrays.toString(input));
        rotate1A(input);
        System.out.printf("output: %s\n", Arrays.toString(input));
        Assert.assertTrue(Arrays.equals(expected, input));
    }

    public void rotate1(int[] A) {

        if (A.length <= 1) {
            return;
        }
        int temp = A[0];
        int len = A.length;
        for (int src = len-1; src > 0; src--) {
            int dest = (src+1) % len;
            A[dest] = A[src];
        }
        A[1] = temp;
    }

    public void rotate1A(int[] A) {

        int last = A.length - 1;
        if (last <= 0) {
            return;
        }
        int temp = A[last];
        for (int dest = last; dest > 0; dest--) {
            A[dest] = A[dest-1];
        }
        A[0] = temp;
    }
}
