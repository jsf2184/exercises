package com.jsf2184.utility;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class SplitTest {
    @Test
    public void testSplit() {
        final String[] parts = "/a/b/c".split("/");
        final int numParts = parts.length;
        Assert.assertEquals(4, numParts);
    }

    @Test
    public void testSplit2() {
        final String[] parts = "/a/b/c/".split("/");
        final int numParts = parts.length;
        Assert.assertEquals(4, numParts);
    }

}
