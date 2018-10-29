package com.jsf2184.cracking;

import org.junit.Assert;
import org.junit.Test;

public class OneAway_1_8 {
    boolean oneAway(String s1, String s2) {
        int lenDiff = s1.length() - s2.length();
        boolean sameLen = false;
        if (lenDiff == 0) {
            return sameLengthDiff(s1, s2);
        }
        if (lenDiff == 1) {
            return diffLengthDiff(s1, s2);
        }
        if (lenDiff == -1) {
            return diffLengthDiff(s2, s1);
        }
        return false;
    }

    boolean sameLengthDiff(String s1, String s2) {
        boolean foundDiff = false;
        for (int i=0; i< s1.length(); i++) {
            if (s1.charAt(i) == s2.charAt(i)) {
                continue;
            }
            if (foundDiff) {
                return false;
            }
            foundDiff = true;
        }
        return true;
    }

    // longStr is one longer than shortStr.
    boolean diffLengthDiff(String longStr, String shortStr) {
        int lidx = 0;
        int sidx = 0;

        boolean foundDiff = false;
        while (lidx < longStr.length() && sidx < shortStr.length()) {
            char lchar = longStr.charAt(lidx);
            char schar = shortStr.charAt(sidx);
            if (lchar == schar) {
                // advance them both
                lidx++;
                sidx++;
            } else {
                if (foundDiff) {
                    return false;
                }
                foundDiff = true;
                lidx++; // simulate insertion into shortStr by just moving on to the next char in longStr and leaving
            }
        }
        return true;
    }

    @Test
    public void testOneInsertGood() {
        Assert.assertTrue(diffLengthDiff("xabc", "abc"));
        Assert.assertTrue(diffLengthDiff("axbc", "abc"));
        Assert.assertTrue(diffLengthDiff("abxc", "abc"));
        Assert.assertTrue(diffLengthDiff("abcx", "abc"));
        Assert.assertTrue(diffLengthDiff("x", ""));
    }
    @Test
    public void testOneInsertBad() {
        Assert.assertFalse(diffLengthDiff("abcd", "xbc"));
        Assert.assertFalse(diffLengthDiff("abcd", "axc"));
        Assert.assertFalse(diffLengthDiff("abcd", "abx"));
    }

    @Test
    public void testCharToNumber() {
        char c = 'a';
        int i = c;
        System.out.println(i);
    }
}
