package com.jsf2184.Codility.Lesson11;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class SievePractice {


    @Test
    public void testCreateLowestFactorArray() {
        int[] lowestFactorArray = createLowestFactorArray(100);
        printLowestFactorArray(lowestFactorArray);
    }

    @Test
    public void testCreateSieve() {
        boolean[] res = createSieve(100);
        System.out.println(Arrays.toString(res));
    }

    @Test
    public void testGetPrimes() {
        System.out.println(getPrimes(100).toString());
    }

    @Test
    public void testGetPrimeFactors() {
        Worker worker = new Worker(100);
        Assert.assertEquals(Arrays.asList(5), worker.getPrimeFactors(5));
        Assert.assertEquals(Arrays.asList(2, 7, 7), worker.getPrimeFactors(98));
        Assert.assertEquals(Arrays.asList(97), worker.getPrimeFactors(97));
        Assert.assertEquals(Arrays.asList(2, 2, 17), worker.getPrimeFactors(68));
    }



    public static List<Integer> getPrimes(int n) {
        boolean[] sieve = createSieve(n);
        List<Integer> res = getPrimes(sieve);
        return res;
    }

    public static List<Integer> getPrimes(boolean[] sieve) {
        List<Integer> res = new ArrayList<>();
        for(int i=0; i<sieve.length; i++) {
            if (sieve[i]) {
                res.add(i);
            }
        }
        return res;
    }

    public static class Worker {
        int[] _lfa;

        public Worker(int n) {
            _lfa = createLowestFactorArray(n);
        }

        public List<Integer> getPrimeFactors(int n) {
            List<Integer> res = getPrimeFactors(n, _lfa);
            return res;
        }

        public static List<Integer> getPrimeFactors(int n, int[] lfa) {
            List<Integer> res = new ArrayList<>();

            // The loop continues until 'n' is a prime number.
            while (lfa[n] != 0) {
                int factor = lfa[n];
                res.add(factor);
                n = n / factor;
            }
            // Add that last prime number to our result.
            res.add(n);
            return res;
        }
    }



    // The Lowest factor array that we are building popululates arr[i] as follows...
    // arr[i] : 0, if 'i' is a prime number
    // arr[i]   p, where 'p' is the smallest prime factor of 'i'

    public static int[] createLowestFactorArray(int n) {
        int[] res = new int[n+1];
        for (int i=2; i * i <= n; i++) {
            if (res[i] == 0) {
                // no point in using i unless it is a prime number since its factors
                // would have already cleared the higher numbers.
                //
                int start = i * i;
                for (int j = start; j <= n; j += i) {
                    if (res[j] == 0) {
                        res[j] = i;
//                        System.out.printf("i=%d, j=%d\n", i, j);
                    }
                }
            }
        }
        return res;
    }

    public static boolean[] createSieve(int n) {
        boolean[] res = new boolean[n+1];
        IntStream.range(1, n+1).forEach(i -> res[i] = true);
        for (int i=2; i * i <= n; i++) {
            if (res[i]) {
                // no point in using i unless it is a prime number since its factors
                // would have already cleared the higher numbers.
                //
                int start = i * i;
                for (int j = start; j <= n; j += i) {
                    res[j] = false;
                    System.out.printf("i=%d, j=%d\n", i, j);
                }
            }
        }
        return res;
    }





    public void printLowestFactorArray(int[] lfa) {
        for (int i=3; i< lfa.length; i++) {
            int lf = lfa[i];
            if (lf == 0) {
                System.out.printf("%d is a prime\n", i);
            } else {
                System.out.printf("lfa[%02d] = %d\n", i, lf);
            }
        }
    }

}
