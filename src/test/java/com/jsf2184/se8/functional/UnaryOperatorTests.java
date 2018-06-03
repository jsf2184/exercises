package com.jsf2184.se8.functional;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.UnaryOperator;

public class UnaryOperatorTests {

    @Test
    public void testUnaryOperator() {
        UnaryOperator<Integer> op = (x) -> x+1;
        Assert.assertEquals(11, op.apply(10).intValue());
    }


}
