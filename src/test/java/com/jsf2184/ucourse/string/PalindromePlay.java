package com.jsf2184.ucourse.string;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class PalindromePlay {

    public static String prepare(String s)  {
        return s.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
    }

    @Test
    public void testPrepare() {
        Assert.assertEquals("abc12d", prepare("abC, #12 [[d"));
    }

    @Test
    public void testIsPalidromeInsideOut() {
        Assert.assertTrue(isPalidromeInsideOut("cabac"));
        Assert.assertTrue(isPalidromeInsideOut("cabbac"));
        Assert.assertTrue(isPalidromeInsideOut("a"));
        Assert.assertTrue(isPalidromeInsideOut("aa"));
        Assert.assertTrue(isPalidromeInsideOut("aba"));
        Assert.assertTrue(isPalidromeInsideOut("abba"));
        Assert.assertFalse(isPalidromeInsideOut("abbz"));
        Assert.assertFalse(isPalidromeInsideOut("abz"));
    }

    @Test
    public void testIsPalidromeOutsideIn() {
        Assert.assertTrue(isPalidromeOutsideIn("cabac"));
        Assert.assertTrue(isPalidromeOutsideIn("cabbac"));
        Assert.assertTrue(isPalidromeOutsideIn("a"));
        Assert.assertTrue(isPalidromeOutsideIn("aa"));
        Assert.assertTrue(isPalidromeOutsideIn("aba"));
        Assert.assertTrue(isPalidromeOutsideIn("abba"));
        Assert.assertFalse(isPalidromeOutsideIn("abbz"));
        Assert.assertFalse(isPalidromeOutsideIn("abz"));
    }

    @Test
    public void testIsPalidromeReverse() {
        Assert.assertTrue(isPalidromeReverse("cabac"));
        Assert.assertTrue(isPalidromeReverse("cabbac"));
        Assert.assertTrue(isPalidromeReverse("a"));
        Assert.assertTrue(isPalidromeReverse("aa"));
        Assert.assertTrue(isPalidromeReverse("aba"));
        Assert.assertTrue(isPalidromeReverse("abba"));
        Assert.assertFalse(isPalidromeReverse("abbz"));
        Assert.assertFalse(isPalidromeReverse("abz"));
    }

    @Test
    public void testCalcPalindromeLen() {
        Assert.assertEquals(0, calcPalindromeLen(0, 0, null));
        Assert.assertEquals(0, calcPalindromeLen(0, 1, null));
        Assert.assertEquals(0, calcPalindromeLen(0, 0, ""));
        Assert.assertEquals(0, calcPalindromeLen(0, 1, ""));
        Assert.assertEquals(1, calcPalindromeLen(0, 0, "a"));
        Assert.assertEquals(0, calcPalindromeLen(0, 1, "a"));
    }

    @Test
    public void testLongestPalidromeInsideOut() {
        Assert.assertEquals(5, calcLongestPalindrome("dcabacz"));
        Assert.assertEquals(6, calcLongestPalindrome("aaaacabbacb"));
        Assert.assertEquals(1, calcLongestPalindrome("xa"));
        Assert.assertEquals(2, calcLongestPalindrome("aac"));
        Assert.assertEquals(3, calcLongestPalindrome("sabat"));
        Assert.assertEquals(4, calcLongestPalindrome("xabbay"));
        Assert.assertEquals(2, calcLongestPalindrome("abbz"));
        Assert.assertEquals(1, calcLongestPalindrome("abz"));
    }

    @Test
    public void testIsAlmostPalindrome() {
        Assert.assertTrue(isAlmostPalindrome("dcabac"));
        Assert.assertTrue(isAlmostPalindrome("cabbaxc"));
        Assert.assertTrue(isAlmostPalindrome("ab"));
        Assert.assertTrue(isAlmostPalindrome("aax"));
        Assert.assertTrue(isAlmostPalindrome("abda"));
        Assert.assertTrue(isAlmostPalindrome("acbba"));
        Assert.assertTrue(isAlmostPalindrome("abbaz"));

        Assert.assertFalse(isAlmostPalindrome("dcaebac"));
        Assert.assertFalse(isAlmostPalindrome("cabbdaxc"));
        Assert.assertFalse(isAlmostPalindrome("abc"));
        Assert.assertFalse(isAlmostPalindrome("aaxx"));
        Assert.assertFalse(isAlmostPalindrome("abdea"));
        Assert.assertFalse(isAlmostPalindrome("acbbac"));
        Assert.assertFalse(isAlmostPalindrome("abbzd"));


    }


    boolean isPalidromeInsideOut(String s) {
        s = prepare(s);
        int len = s.length();
        int half = len/2;
        int palindromeLen;
        if (len %2 == 0) {
            // "abba", len = 4, left = 1, right = 2
            palindromeLen = calcPalindromeLen(half -1, half, s);
        } else {
            // "aba", len = 3, left = 1, right = 1
            palindromeLen = calcPalindromeLen(half, half, s);
        }
        return  palindromeLen == len;
    }


    boolean isAlmostPalindrome(String s) {
        s = prepare(s);
        int len = s.length();
        int left = 0;
        int right = len-1;
        while (left <= right) {
            if (s.charAt(left) != s.charAt(right)) {
                return isPalidromeOutsideIn(left+1, right, s) || isPalidromeOutsideIn(left, right-1, s);
            }
            left++;
            right--;
        }
        return true;
    }

    boolean isPalidromeOutsideIn(String s) {
        s = prepare(s);
        int len = s.length();
        int left = 0;
        int right = len-1;
        return isPalidromeOutsideIn(left, right, s);
    }

    boolean isPalidromeOutsideIn(int left, int right, String s) {
        while (left <= right) {
            if (s.charAt(left) != s.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }


    boolean isPalidromeReverse(String s) {
        s = prepare(s);
        final String reverse = StringUtils.reverse(s);
        return s.equals(reverse);
    }

    public int calcLongestPalindrome(String s) {
        s = prepare(s);
        int longest = 0;
        for (int i=0; i<s.length();i++) {
            longest = Math.max(longest, calcPalindromeLen(i, i, s));
            longest = Math.max(longest, calcPalindromeLen(i, i+1, s));
        }
        return longest;

    }

    int calcPalindromeLen(int left, int right, String s) {
        if (s == null) {
            return 0;
        }
        int len = 0;
        while(left >=0 && right < s.length()) {
            if (s.charAt(left) == s.charAt(right)) {
                len = (right - left) + 1;
            } else {
                break;
            }
            left--;
            right++;
        }
        return len;
    }
}
