package com.jsf2184;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BigDecimalTests {
    @Test
    public void testTwoIntConstructor() {
        BigDecimal sut = new BigDecimal(new BigInteger("-105"), 3);
        printParts(sut);
        printParts(new BigDecimal(".00000123"));
        printParts(new BigDecimal("1234.5678"));
    }

    public void printParts(BigDecimal sut) {
        System.out.printf("sut = %s, sut.scale=%s, unscaledVal=%s \n",
                          sut,
                          sut.scale(),
                          sut.unscaledValue());
    }
}
