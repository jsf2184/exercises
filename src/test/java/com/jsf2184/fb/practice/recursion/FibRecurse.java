package com.jsf2184.fb.practice.recursion;

import org.junit.Assert;
import org.junit.Test;

public class FibRecurse {

    public static int steps = 0;
    @Test
    public void testIt() {

        // n:      0 1 2 3 4 5 6  7  8  9 10
        // result: 0 1 1 2 3 5 8 13 21 34 55
        steps = 0;
        int result = fib(10);
        Assert.assertEquals(55, result);
        System.out.println(steps);
    }

    @Test
    public void testOptimal() {

        // n:      0 1 2 3 4 5 6  7  8  9 10
        // result: 0 1 1 2 3 5 8 13 21 34 55
        steps = 0;
        int result = fibOptimal(10);
        Assert.assertEquals(55, result);
        System.out.println(steps);
    }

    public int fib(int n) {
        steps++;
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        return fib(n-1) + fib(n-2);
    }

    public int fibOptimal(int n) {
        int[] results = new int[n+1];
        final int result = fibOptimal(n, results);
        return result;
    }

    public int fibOptimal(int n, int[] results) {
        System.out.printf("fibOptimal called for n=%d\n", n);
        steps++;
        if (n == 0) {
            return 0;
        }
        if (results[n] != 0) {
            return results[n];
        }
        int result;
        if (n == 1) {
            result =  1;
        } else {
            final int subProblem1 = n - 1;
            final int subProblem2 = n - 2;
            final int answer1 = results[subProblem1] == 0 ? fibOptimal(subProblem1, results) : results[subProblem1];
            final int answer2 = results[subProblem2] == 0 ? fibOptimal(subProblem2, results) : results[subProblem2];
            result = answer1 + answer2;
        }
        results[n] = result;
        return result;
    }


}
