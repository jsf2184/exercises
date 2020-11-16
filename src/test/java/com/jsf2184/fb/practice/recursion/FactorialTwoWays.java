package com.jsf2184.fb.practice.recursion;

import org.junit.Assert;
import org.junit.Test;

public class FactorialTwoWays {
    @Test
    public void testIt() {
        final int pfxResult = factWithPfx(1, 10);
        final int withoutPfxResult = fact(10);
        Assert.assertEquals(pfxResult, withoutPfxResult);
    }

    public int factWithPfx(int pfx, int n) {
        if (n == 1) {
            return pfx;
        }
        return factWithPfx(pfx*n, n-1);
    }

    public int fact(int n) {
        if (n == 1) {
            return 1;
        }
        return n * fact(n-1);
    }

}
