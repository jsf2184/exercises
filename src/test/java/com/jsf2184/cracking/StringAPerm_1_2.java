package com.jsf2184.cracking;

import org.junit.Assert;
import org.junit.Test;

public class StringAPerm_1_2 {
    public static boolean isPerm(String s1, String s2) {
        int len = s1.length();
        if (len != s2.length()) {
            return false;
        }
        int[] counts = new int[256];
        s1.chars().forEach(c -> counts[c] += 1);

        boolean badPerm = s2.chars().anyMatch(c -> {
            counts[c] -= 1;
            if (counts[c] < 0) {
                return true;
            }
            return false;
        });

        return !badPerm;

    }

    @Test
    public void testCases() {
        Assert.assertFalse(isPerm("abca", "aaaa"));
        Assert.assertTrue(isPerm("abc", "cba"));
        Assert.assertFalse(isPerm("abcd", "cbae"));
    }
}
