package com.jsf2184;

import org.junit.Assert;
import org.junit.Test;

public class BitPlay {
    @Test
    public void powerOfTwo() {
        Assert.assertEquals(1, pow2(0));
        Assert.assertEquals(2, pow2(1));
        Assert.assertEquals(4, pow2(2));
        Assert.assertEquals(8, pow2(3));
        Assert.assertEquals(16, pow2(4));
    }

    int pow2(int n) {
        int v = 1;
        int result = v << n;
        return result;

    }
}
