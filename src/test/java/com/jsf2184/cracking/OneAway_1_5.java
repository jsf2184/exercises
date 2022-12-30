package com.jsf2184.cracking;

import org.junit.Assert;
import org.junit.Test;

public class OneAway_1_5 {

    public boolean oneAway(String s1, String s2) {
        int lenDiff = s2.length() - s1.length();
        if (Math.abs(lenDiff) > 1) return false;

        boolean sameLength = s1.length() == s2.length();

        String longer = s2;
        String shorter = s1;
        if (s1.length() >= s2.length()) {
            longer = s1;
            shorter = s2;
        }
        boolean foundDiff = false;
        int sidx;
        int lidx;
        for (sidx=0, lidx = 0; sidx < shorter.length(); ) {
            if (shorter.charAt(sidx) == longer.charAt(lidx)) {
                sidx++;
                lidx++;
                continue;
            }
            if (foundDiff) {return false;}
            foundDiff = true;
            if (sameLength) {
                sidx++;
                lidx++;
            } else {
                lidx++;
            }
        }
        if (foundDiff) {
            return lidx == longer.length();
        }
        return true;
    }

    @Test
    public void testIt() {
//        Assert.assertTrue(oneAway("abc", "abc"));
//        Assert.assertTrue(oneAway("abc", "abcd"));
//        Assert.assertTrue(oneAway("abcd", "abc"));
        Assert.assertFalse(oneAway("abcd", "abcef"));
    }

}
