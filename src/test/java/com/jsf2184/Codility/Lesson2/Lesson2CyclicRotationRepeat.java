package com.jsf2184.Codility.Lesson2;

import org.junit.Assert;
import org.junit.Test;

import java.util.stream.IntStream;

public class Lesson2CyclicRotationRepeat {
    public int[] solution(int[] A, int K) {
        int length = A.length;
        int actual = K % length;
        if (actual == 0) {
            return A;
        }
        IntStream.range(0, actual).forEach((i) -> move1(A));
        return A;
    }

    @SuppressWarnings("ManualArrayCopy")
    public int[] move1(int[] A) {
        int length = A.length;
        if (length <= 1) {
            return A;
        }
        int lastIdx = length -1;
        int lastVal = A[lastIdx];
        for (int srcIdx = lastIdx-1; srcIdx >=0; srcIdx--) {
            A[srcIdx+1] = A[srcIdx];
        }
        A[0] = lastVal;
        return A;
    }

    @Test
    public void testMove1OnEmptyArray() {
        int A[] = {};
        int[] res = move1(A);
        Assert.assertArrayEquals(new int[]{}, res);
    }

    @Test
    public void testMove1OnSize1Array() {
        int A[] = {0};
        int[] res = move1(A);
        Assert.assertArrayEquals(new int[]{0}, res);
    }

    @Test
    public void testMove1OnSize3Array() {
        int A[] = {0, 1, 2};
        int[] res = move1(A);
        Assert.assertArrayEquals(new int[]{2, 0, 1}, res);
    }

    @Test
    public void testSolution() {
        int A[] = {0, 1, 2, 3, 4, 5};
        int[] res = solution(A, 3);
        Assert.assertArrayEquals(new int[]{3, 4, 5, 0, 1, 2}, res);

    }

}