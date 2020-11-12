package com.jsf2184.hackerrank.capital;

import org.junit.Assert;
import org.junit.Test;
import java.util.Random;
import static com.jsf2184.hackerrank.capital.DivUtility.getRandomString;

public class DivisibleStrings {


    public static int findSmallestDivisor(String s, String t) {

        if (! isDivisible(s, t)) {
            return -1;
        }

        // Now that we know s is divisible by t, we can focus on finding the smallest
        // String divisible into both s and t. One simple optimization is that we
        // only have to focus on finding the smallest divisible into 't' since we
        // know it will also be divisible into 's'.
        //
        return findSmallestDivisor(t);

    }

    private static int findSmallestDivisor(String s) {
        // We are going to look for ths smallest 't' that is divisible into 's'.
        // Of course we could just start at size 1 and keep growing our 't' by 1 until we find one
        // that works but we will try to grow a little faster than that where we can to
        // optimize our solution.
        //
        int sLength = s.length();
        for (int tLength = 1; tLength <= sLength/2; ) {
            // note we can start looking for the 1st t chars at an offset of 't' since
            // we know that the first 't' characters of 's' are guaranteed to match itself
            if (sLength % tLength != 0) {
                tLength++;
            } else {
                int consumedCount = countConsumedMatchedChars(s, tLength, s, tLength);
                if (consumedCount == sLength - tLength) {
                    return tLength;
                }
                if (consumedCount == 0) {
                    consumedCount = 1;
                }
                tLength += consumedCount;
            }
        }
        return sLength;

    }

    private static int findSmallestDivisorSlow(String s) {
        // We are going to look for ths smallest 't' that is divisible into 's'.
        // Of course we could just start at size 1 and keep growing our 't' by 1 until we find one
        // that works but we will try to grow a little faster than that where we can to
        // optimize our solution.
        //
        int sLength = s.length();
        for (int tLength = 1; tLength < sLength; tLength++ ) {
            // note we can start looking for the 1st t chars at an offset of 't' since
            // we know that the first 't' characters of 's' are guaranteed to match itself
            if (sLength % tLength != 0) {
                continue;
            }
            int consumedCount = countConsumedMatchedChars(s, tLength, s, tLength);
            if (consumedCount == sLength - tLength) {
                return tLength;
            }
        }
        return sLength;
    }


    private static int findSmallestDivisorModulo(String s) {
        // We are going to look for ths smallest 't' that is divisible into 's'.
        // Of course we could just start at size 1 and keep growing our 't' by 1 until we find one
        // that works but we will try to grow a little faster than that where we can to
        // optimize our solution.
        //
        int sLength = s.length();
        for (int tLength = 1; tLength <= sLength/2; tLength++ ) {
            if (isDivisible2(s, tLength)){
                return tLength;
            }
        }
        return sLength;
    }


    public static boolean isDivisible2(String s, int tLength) {
        int sLength = s.length();
        if (sLength % tLength != 0) {
            return false;
        }
        for (int i=tLength, j=0; i< sLength; i++, j++) {
            if (j== tLength)
            {
                j=0;
            }
            if (s.charAt(i) != s.charAt(j)) {
                return false;
            }
        }
        return true;

    }



    public static boolean isDivisible(String s, String t) {
        // use our common implementation of isDivisible() where we start at the beginning of 's'
        // and just see if all of 't' isDivisible() into 's'/

        int sLength = s.length();
        int tLength = t.length();
        if (sLength % tLength != 0) {
            return false;
        }
        int consumedCount = countConsumedMatchedChars(s, 0, t, tLength);
        return  consumedCount == sLength;
    }

    // This more general signature of isDivisible() checks whether the first 'tLength' chars of 't' are
    // divisible into 's'. This is useful when we are looking for the smallest factors of s and allows us
    // to try various substrings of 's' as possibilities without having to copy those substrings into a separate
    // String. This approach helps us write more efficient (less wasteful) code.
    //
    public static boolean isDivisible(String s, int sBegin, String t, int tLength) {
        // The only hope of s being divisible by t is if the length of s is divisible by the length of t.
        int sLength = s.length();
        if (sLength % tLength != 0) {
            return false;
        }
        int consumedCount = countConsumedMatchedChars(s, sBegin, t, tLength);
        boolean res = consumedCount == sLength;
        return res;
    }

    static int countConsumedMatchedChars(String s, int sBegin, String t, int tLength) {
        int sLength = s.length();
        int result = 0;
        for (; sBegin < sLength; sBegin += tLength) {
            int matchedCharCount = countMatchedChars(s, sBegin, tLength, t);
            result += matchedCharCount;
            if (matchedCharCount != tLength) {
                break;
            }
        }
        return result;
    }


    // See if the first 'tLength' characters of 't' match the characters of 's' starting at 'sBegin'.
    // Note that by comparing characters in place, we avoid any character or string copying
    // which helps our performance.
    //
    public static boolean charsMatch(String s, int sBegin, int tLength, String t) {
        return countMatchedChars(s, sBegin, tLength, t) == tLength;
    }

    public static int countMatchedChars(String s, int sBegin, int tLength, String t) {
        int tIdx;
        int sIdx;
        for (sIdx=sBegin, tIdx=0; tIdx<tLength; tIdx++, sIdx++) {
            if (t.charAt(tIdx) != s.charAt(sIdx)) {
                return tIdx;
            }
        }
        return tIdx;
    }


    @Test
    public void testFindSmallestDivisor() {
        Assert.assertEquals(1, findSmallestDivisorSlow("a"));
        Assert.assertEquals(1, findSmallestDivisorSlow("aa"));
        Assert.assertEquals(2, findSmallestDivisorSlow("ab"));

        Assert.assertEquals(4, findSmallestDivisorSlow("abcd"));
        Assert.assertEquals(1, findSmallestDivisorSlow("aaaa"));
        Assert.assertEquals(4, findSmallestDivisorSlow("aaad"));
        Assert.assertEquals(2, findSmallestDivisorSlow("abab"));
        Assert.assertEquals(6, findSmallestDivisorSlow("abcabdabcabd"));
        Assert.assertEquals(9, findSmallestDivisorSlow("abcabcabdabcabcabd"));
    }

    @Test
    public void testFindSmallestDivisorMod() {
        Assert.assertEquals(1, findSmallestDivisorModulo("a"));
        Assert.assertEquals(1, findSmallestDivisorModulo("aa"));
        Assert.assertEquals(2, findSmallestDivisorModulo("ab"));

        Assert.assertEquals(4, findSmallestDivisorModulo("abcd"));
        Assert.assertEquals(1, findSmallestDivisorModulo("aaaa"));
        Assert.assertEquals(4, findSmallestDivisorModulo("aaad"));
        Assert.assertEquals(2, findSmallestDivisorModulo("abab"));
        Assert.assertEquals(6, findSmallestDivisorModulo("abcabdabcabd"));
        Assert.assertEquals(9, findSmallestDivisorModulo("abcabcabdabcabcabd"));
    }


    @Test
    public void testCompareAnswers() {
        compareAnswers("acadacacadac");
        compareAnswers("abaaba");
    }

    @Test
    public void testFindSmallestDivisorBothWays() {
        Random random = new Random();
        int i=0;
        int slen = 12;
        while (i < 1000) {
            String s = DivUtility.getRandomString(random, slen);
            int optimalAnswer = findSmallestDivisorModulo(s);
            int slowAnswer = findSmallestDivisorSlow(s);
            if (slowAnswer != slen) {
                i++;
                System.out.printf("With '%s', optimal = %d, slow = %d\n", s, optimalAnswer, slowAnswer);
                Assert.assertEquals(slowAnswer, optimalAnswer);
            }
        }
    }

    public static void compareAnswers(String s) {
        int optimalAnswer = findSmallestDivisorModulo(s);
        int slowAnswer = findSmallestDivisorSlow(s);
        System.out.printf("With '%s', optimal = %d, slow = %d\n", s, optimalAnswer, slowAnswer);
        Assert.assertEquals(slowAnswer, optimalAnswer);

    }


    @Test
    public void testCharsMatch() {
        Assert.assertTrue(charsMatch("abcabc", 3, 3, "abc"));
        Assert.assertTrue(charsMatch("abcabc", 3, 3, "abcabd"));
        Assert.assertFalse(charsMatch("abcabd", 3, 3, "abc"));
    }

    @Test
    public void testIsDivisibleTrue() {
        Assert.assertTrue(isDivisible("abcabc", "abc"));
        Assert.assertTrue(isDivisible("abcabc", "abcabc"));
    }

    @Test
    public void testIsDivisibleFalse() {
        Assert.assertFalse(isDivisible("a", "b"));
        Assert.assertFalse(isDivisible("ab", "ac"));
        Assert.assertFalse(isDivisible("abcabcd", "abc"));
        Assert.assertFalse(isDivisible("abcabd", "abc"));
    }

}
