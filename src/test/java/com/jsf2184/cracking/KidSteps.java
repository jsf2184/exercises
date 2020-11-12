package com.jsf2184.cracking;

import org.junit.Assert;
import org.junit.Test;

import java.util.stream.IntStream;

public class KidSteps {

    public long countSteps(int n) {
        int size = n + 1;
        long[] cache = new long[size];
        IntStream.range(0, size).forEach(i -> cache[i] = -1);
        return countSteps(n, cache);
    }
    public long countSteps(int n, long[] cache) {
        if (n == 0) {
            return 1;
        }
        if (n < 0) {
            return 0;
        }
        if (cache[n]>=0) {
            System.out.println("leverage");
            return cache[n];
        }
        long result = countSteps(n-3, cache) + countSteps(n-2, cache) + countSteps(n-1, cache);
        cache[n] = result;
        System.out.println("manual");
        return result;
    }

    public long countStepsOld(int n) {
        if (n == 0) {
            return 1;
        }
        if (n < 0) {
            return 0;
        }
        long result = countStepsOld(n-3) + countStepsOld(n-2) + countStepsOld(n-1);
        return result;
    }


    @Test
    public void testIt() {

        for (int n=0; n< 10; n++) {
            System.out.println("");
            long optimal = countSteps(n);
            long basic = countStepsOld(n);
            System.out.printf("With n = %d, optimal = %d, basic = %d\n", n, optimal, basic);
            Assert.assertEquals(optimal, basic);

        }

    }


    static boolean[] eliminations;

    public static void main(String args[]) {
        System.out.println(countPrimes(100));
    }

    public static int countPrimes(int n)
    {
        eliminations = new boolean[n+1];
        int result = 0;
        for (int i=2; i <= n; i++) {
            if (isPrime(i)) {
                result++;
            }
        }
        return result;
    }

    public static boolean isPrime(int value) {

        if (eliminations[value]) {
            return false;
        }
        // If it wasn't eliminated by any of those smaller numbers that precede 'value', it must be prime.
        // Use it to eliminate every multiple of this newly found prime number.
        for (int j=value * 2; j<eliminations.length ; j+=value) {
            eliminations[j] = true;
        }
//      System.out.printf("%d is prime\n", value);
        return true;
    }
}
