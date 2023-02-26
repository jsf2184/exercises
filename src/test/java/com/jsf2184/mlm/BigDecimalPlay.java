package com.jsf2184.mlm;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class BigDecimalPlay {
    @Test
    public void play() {
        BigDecimal pi2 = BigDecimal.valueOf(314, 2);
        Assert.assertEquals(pi2.toString(), "3.14");
        Assert.assertEquals(pi2.unscaledValue().intValue(), 314);
        Assert.assertEquals(pi2.scale(), 2);
        Assert.assertEquals(pi2.precision(), 3);

        BigDecimal pi3 = BigDecimal.valueOf(3140, 3);
        Assert.assertEquals(pi3.toString(), "3.140");
        Assert.assertEquals(pi3.unscaledValue().intValue(), 3140);
        Assert.assertEquals(pi3.scale(), 3);
        Assert.assertEquals(pi3.precision(), 4);

        Assert.assertFalse(pi2.equals(pi3));
        Assert.assertEquals(pi2.compareTo(pi3), 0);
    }
}
