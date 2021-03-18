package com.jsf2184.wf;

import org.junit.Assert;
import org.junit.Test;

public class Palindrome {


    @Test
    public void testLongest() {
        Assert.assertEquals(1, longestPalindrome("a"));
        Assert.assertEquals(1, longestPalindrome("ab"));
        Assert.assertEquals(1, longestPalindrome("abc"));

        Assert.assertEquals(2, longestPalindrome("aa"));
        Assert.assertEquals(2, longestPalindrome("xyzaa"));
        Assert.assertEquals(2, longestPalindrome("aaxyz"));

        Assert.assertEquals(3, longestPalindrome("aba"));
        Assert.assertEquals(3, longestPalindrome("xyzaba"));
        Assert.assertEquals(3, longestPalindrome("abaxyz"));

        Assert.assertEquals(4, longestPalindrome("abacddc"));
        Assert.assertEquals(4, longestPalindrome("cddcxyzaba"));
        Assert.assertEquals(4, longestPalindrome("abacddcxyz"));


    }

    @Test
    public void testIsPalindrome() {
        Assert.assertTrue(isPalindrome("abba"));
        Assert.assertTrue(isPalindrome("abcba"));
        Assert.assertFalse(isPalindrome("abbax"));
        Assert.assertFalse(isPalindrome("xabcba"));
        Assert.assertFalse(isPalindrome("xabba"));
        Assert.assertFalse(isPalindrome("xxabcb"));
    }

    boolean isPalindrome(String s) {
        int len = s.length();
        int mid = len / 2;
        int plen;
        if (len %2 == 0) {
            //even len
            plen = longestPalindrome(mid-1, mid, s);
        } else {
            // odd len
            plen = longestPalindrome(mid-1, mid+1, s);
        }
        return plen == len;
    }

    int longestPalindrome(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        int longest = 0;
        for (int i=0; i<s.length(); i++) {
            longest = Math.max(longest, longestPalindrome(i, i+1, s));
            longest = Math.max(longest, longestPalindrome(i, i+2, s));
        }
        return longest;
    }

    int longestPalindrome(int left, int right, String s) {
        right = Math.min(s.length()-1, right);
        int length = right - left - 1;
        while (left >=0 && right < s.length()) {
            if (s.charAt(left) != s.charAt(right)) {
                break;
            }
            left -=1;
            right += 1;
            length += 2;
        }
        return length;
    }
}
