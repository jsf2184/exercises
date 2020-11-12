package com.jsf2184;

import org.junit.Assert;
import org.junit.Test;

public class ZackCompareTest {
    @Test
    public void testStringComparExpression() {
        callBoth(1, "abcdefg", true); // should be true
        callBoth(10, "abcdefg", false); // should be false

    }

    public static void callBoth(int i, String s, boolean expected) {
        final boolean withoutParens = compareStringLengthWithoutParens(i, s);
        final boolean withParens = compareStringLengthWithParens(i, s);
        Assert.assertEquals(expected, withoutParens);
        Assert.assertEquals(expected, withParens);
    }
    public static boolean compareStringLengthWithoutParens(int i, String s) {
        boolean result = i < s.length() - 4;
        return result;
    }
    public static boolean compareStringLengthWithParens(int i, String s) {
        boolean result = i < (s.length() - 4);
        return result;
    }

}
