package com.jsf2184.utility;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Fib {

    public int calc(int n) {
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        int res = 1;
        int prev = 0;
        for (int i=2; i<=n; i++) {
            int temp = res;
            res += prev;
            prev = temp;
        }
        return res;
    }

    public int calcWithRange(int n) {
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        final AtomicInteger res = new AtomicInteger(1);
        final AtomicInteger prev = new AtomicInteger(0);
        IntStream.range(2, n+1).forEach((x) -> {
            int temp = res.get();
            res.addAndGet(prev.get());
            prev.set(temp);
        });
        return res.get();
    }



    public int calcRecurse(int n) {
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }

        return calcRecurse(n-1) + calcRecurse(n-2);
    }

    @Test
    public void testFib() {
        for (int n=0; n<10; n++) {
            System.out.printf("n = %d, res = %d\n", n, calc(n));
        }
    }


    @Test
    public void testFibRecurse() {
        for (int n=0; n<10; n++) {
            System.out.printf("n = %d, res = %d\n", n, calcRecurse(n));
        }
    }

    @Test
    public void testFibRange() {
        for (int n=0; n<10; n++) {
            System.out.printf("n = %d, res = %d\n", n, calcWithRange(n));
        }
    }


}
