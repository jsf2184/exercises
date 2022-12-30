package com.jsf2184.cracking;

import org.junit.Assert;
import org.junit.Test;

public class PermOfPalindrome_1_4 {

    boolean isPermOfPalindrome(String s) {
        boolean[] oddCounts = new boolean[256];
        int numOdds = 0;
        for (int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            boolean wasOdd = oddCounts[c];
            if (wasOdd) {
                numOdds -= 1;
            } else {
                numOdds += 1;
            }
            oddCounts[c] = !wasOdd;
        }
        if (s.length() %2 == 0) {
            return numOdds == 0;
        }
        return  numOdds == 1;
    }

    @Test
    public void tests() {
//        Assert.assertTrue(isPermOfPalindrome("a"));
//        Assert.assertTrue(isPermOfPalindrome("aab"));
//        Assert.assertTrue(isPermOfPalindrome("aa"));
//        Assert.assertTrue(isPermOfPalindrome("aab"));
        Assert.assertFalse(isPermOfPalindrome("aaabbb"));
        Assert.assertFalse(isPermOfPalindrome("aaaabbbc"));

    }
}
