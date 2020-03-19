package com.jsf2184.hackerrank;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SockSort {
    static int sockMerchant(int n, int[] ar) {
        Map<Integer, Integer> map = new HashMap<>();
        int size = Math.min(n, ar.length);
        for (int i=0; i<size; i++)
        {
            int v = ar[i];
            int count = map.computeIfAbsent(v, k -> 0);
            map.put(v, count+1);
        }

        AtomicInteger pairInt = new AtomicInteger(0);
        map.values().forEach(count -> pairInt.addAndGet(count/2));
        return pairInt.intValue();
    }

    @Test
    public void testSockMerchant()
    {
        int[] input = {10, 20, 20, 10, 10, 30, 50, 10, 20};
        Assert.assertEquals(3, sockMerchant(input.length, input));
    }

}
