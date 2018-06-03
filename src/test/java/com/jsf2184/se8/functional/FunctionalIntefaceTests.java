package com.jsf2184.se8.functional;

import org.junit.Assert;
import org.junit.Test;

public class FunctionalIntefaceTests {
    @FunctionalInterface
    interface ThreeIntInterface {
        int call(int x, int y, int z);
        // note for functional interfaces, it is ok to have default functions.
        default void f() {System.out.println("Hi");};

        // but not non-default ones.
//        void g(); // have to comment this non-default function out.

    }

    public int useThreeIntInterface(ThreeIntInterface tif, int x, int y, int z) {
        tif.f();
        int res = tif.call(x, y, z);
        return res;
    }

    @Test
    public void testTifWithLambda() {
        Assert.assertEquals(6,  useThreeIntInterface((x, y, z) -> {return x+y+z;}, 1, 2, 3));
    }
}
