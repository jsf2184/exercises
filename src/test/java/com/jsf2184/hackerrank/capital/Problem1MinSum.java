package com.jsf2184.hackerrank.capital;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class Problem1MinSum {

    public static int minSum(List<Integer> list, int k) {

        // Given constraints of n <= 100,000 and list[i] < 10,000 an
        // integer is sufficient to sum all the elements.
        //
        int currentSum = 0;

        // We'll want to always work with the largest value in the collection and a PriorityQueue is
        // ideal for that. Let's start by loading it up and calculating our current sum as we go.
        //
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>((x,y) -> y-x );
        for (Integer v : list) {
            currentSum += v;
            priorityQueue.add(v);
        }

        System.out.printf("Init State: currentSum=%d: %s\n", currentSum, priorityQueue);

        for (int op=0; op<k; op++) {

            // The biggest potential to reduce the sum is in the largest number which
            // is easy to get from a PriorityQueue.
            //
            Integer largest = priorityQueue.poll();

            currentSum -= largest;

            // Efficient way to do integer division by 2 with roundup.
            int half = (largest+1) >> 1;

            // Insert half back into the priorityQueue and update currentSum
            priorityQueue.add(half);
            currentSum += half;

            System.out.printf("op=%d, currentSum=%d: %s\n", op, currentSum, priorityQueue);
        }

        return currentSum;


    }

    public static int calcHalf(int v) {
        int half = (v+1) >> 1;
        return half;
    }

    @Test
    public void testHalf() {
        Assert.assertEquals(10, calcHalf(19));
        Assert.assertEquals(10, calcHalf(20));
    }


    @Test
    public void runTest() {
        testIt(Arrays.asList(10, 20, 7), 4, 14);
        testIt(Collections.singletonList(2), 1, 1);
        testIt(Arrays.asList(2, 3), 1, 4);
    }
    public static void testIt(List<Integer> list, int k, int expected)
    {
        int res = minSum(list, k);
        Assert.assertEquals(expected, res);
    }
}
