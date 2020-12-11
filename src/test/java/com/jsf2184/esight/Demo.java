package com.jsf2184.esight;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class Demo {
    public int solution(int[] A) {
        // write your code in Java SE 8
        final List<Integer> list = new ArrayList<>();
        for (int a : A) {
            if (a > 0) {
                list.add(a);
            }
        }
        Collections.sort(list);
        int prev = 0;
        for (Integer s : list) {
            if (s > prev+1) {
                return prev+1;
            }
            prev = s;
        }
        return prev+1;
    }

    public int solution2(int[] A) {
        // write your code in Java SE 8
        TreeSet<Integer> set = new TreeSet<>();
        for (int i=0; i<A.length; i++) {
            final int v = A[i];
            if (v > 0) {
                set.add(v);
            }
        }
        int prev = 0;
        for (Integer s : set) {
            if (s > prev+1) {
                return prev+1;
            }
            prev = s;
        }
        return prev+1;
    }


    @Test
    public void testIt() {
        Assert.assertEquals(3, solution(new int[] {1,2,4,5}));
        Assert.assertEquals(1, solution(new int[] {2,3, 4,5}));
        Assert.assertEquals(6, solution(new int[] {3, 4,5, 1, 2}));
        Assert.assertEquals(1, solution(new int[] {-1, -3}));
    }

}
