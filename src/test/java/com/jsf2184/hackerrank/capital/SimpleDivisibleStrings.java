package com.jsf2184.hackerrank.capital;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static com.jsf2184.hackerrank.capital.DivUtility.getRandomString;

public class SimpleDivisibleStrings {


    // *************************************************
    // *************************************************
    // A few notes for those reading this code.
    //
    // A very simple, easy to understand implementation of this code would a little easier to
    // understand than this implementation which while trying to be clear, also tries to be
    // performant. So, for example, to avoid character copying, we don't create any substrings
    // but instead reference ranges of characters within Strings.
    //
    // The code contains other optimizations which are explained as best as I can where they appear.
    //
    // *************************************************
    // *************************************************


    /**
     *
     * @param s - String s
     * @param t - String t
     * @return
     *   -1 if 's' is not divisible by 't'
     *      or
     *   the length of the smallest substring that is divisible into both 's' and 't'.
     */
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

    /**
     *
     * @param s - String s
     * @return the length of the smallest divisor String that goes evenly into 's'
     */
    private static int findSmallestDivisor(String s) {


        int sLength = s.length();

        // ** OPTIMIZATION **
        // A substring at the beginning of 's' has no chance of being divisible into 's' itself unless that
        // substring has at least one of every character that constitues 's'. We'll start by finding
        // the length of the smallest substring that has at least one of every character in 's'.
        //
        int smallestLength = getSmallestSubstringLengthThatIncludesAllChars(s);

        // ** OPTIMIZATION **
        // As the substrings that we try, grow larger, they have no chance of being divisible into 's'
        // once they are larger than half the length of 's'.
        //
        int halfLength = sLength / 2;
        for (int substrLength = smallestLength; substrLength <= halfLength; substrLength++ ) {
            // note we can start looking for the 1st t chars at an offset of 't' since
            // we know that the first 't' characters of 's' are guaranteed to match itself
            if (isDivisible(s, substrLength, s, substrLength)) {
                return  substrLength;
            }
        }
        return sLength;
    }

    /**
     *
     * @param s - String s
     * @return the length of the smallest starting substring that contains
     *         at least one of every character that is within 's'
     */
    private static int getSmallestSubstringLengthThatIncludesAllChars(String s) {
        Set<Character> charsSeen = new HashSet<>();
        int lastIdx = 0;
        for (int i=0; i< s.length(); i++) {
            char c = s.charAt(i);
            if (!charsSeen.contains(c)) {
                // this is a char we've never seen
                charsSeen.add(c);
                lastIdx = i;
            }
        }
        return lastIdx + 1;
    }

    /**
     *
     * @param s - String s
     * @param t - String t
     * @return true if 's' is divisible by 't'
     */
    public static boolean isDivisible(String s, String t) {
        // use our common implementation of isDivisible() where we start at the beginning of 's'
        // and just see if all of 't' isDivisible() into 's'/
        return isDivisible(s, 0, t, t.length());
    }

    /**
     *
     *  This more general signature of isDivisible() checks whether the first 'tLength' chars of 't' are
     *  divisible into 's' (begining at offset sOffset). This is useful when we are looking for the
     *  smallest factors of s and allows us to try various substrings of 's' as possibilities without
     *  having to copy those substrings into a separate String. This approach helps us write more
     *  efficient (less wasteful) code.
     *
     * @param s: String s
     * @param sOffset: an offset into String 's'
     * @param t: String t
     * @param tLength: A length specifying the length of a substring at the start of 't'
     * @return true if the first tLength characters of 't' constitute a substring that is divisible
     *         into 's' starting at offset 'sOffset'.
     */
    public static boolean isDivisible(String s, int sOffset, String t, int tLength) {
        // ** OPTIMIZATION **
        // The only hope of s being divisible by t is if the length of s is divisible by the length of t.
        int sLength = s.length();
        if (sLength % tLength != 0) {
            return false;
        }
        for (; sOffset < sLength; sOffset += tLength) {
            if (!s.regionMatches(sOffset, t, 0, tLength)) {
                return false;
            }
        }
        return true;
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
            if (isDivisible(s, tLength, s, tLength)) {
                return  tLength;
            }
        }
        return sLength;
    }


    @Test
    public void testFindSmallestDivisor() {
        Assert.assertEquals(1, findSmallestDivisor("a"));
        Assert.assertEquals(1, findSmallestDivisor("aa"));
        Assert.assertEquals(2, findSmallestDivisor("ab"));

        Assert.assertEquals(4, findSmallestDivisor("abcd"));
        Assert.assertEquals(1, findSmallestDivisor("aaaa"));
        Assert.assertEquals(4, findSmallestDivisor("aaad"));
        Assert.assertEquals(2, findSmallestDivisor("abab"));
        Assert.assertEquals(6, findSmallestDivisor("abcabdabcabd"));
        Assert.assertEquals(9, findSmallestDivisor("abcabcabdabcabcabd"));
        Assert.assertEquals(6, findSmallestDivisor("acadacacadac"));
        Assert.assertEquals(3, findSmallestDivisor("abaaba"));
    }

    @Test
    public void testCharsMatch() {
        Assert.assertTrue("abcabc".regionMatches(3, "abc", 0, 3));
        Assert.assertTrue("abcabc".regionMatches(3, "abcabd", 0, 3));
        Assert.assertFalse("abcabd".regionMatches(3, "abc", 0, 3));
    }

    @Test
    public void testIsDivisibleTrue() {
        Assert.assertTrue(isDivisible("abcabc", "abc"));
        Assert.assertTrue(isDivisible("abcabc", "abcabc"));
    }

    @Test
    public void testIsDivisibleFalse() {
        Assert.assertFalse(isDivisible("abcabcd", "abc"));
        Assert.assertFalse(isDivisible("abcabd", "abc"));
    }

    @Test
    public void testFindSmallestDivisorBothWays() {
        Random random = new Random();
        int i=0;
        int slen = 12;
        while (i < 100) {
            String s = getRandomString(random, slen);
            int optimalAnswer = findSmallestDivisor(s);
            int slowAnswer = findSmallestDivisorSlow(s);
            if (slowAnswer != slen) {
                i++;
                System.out.printf("With '%s', optimal = %d, slow = %d\n", s, optimalAnswer, slowAnswer);
                Assert.assertEquals(slowAnswer, optimalAnswer);
            }
        }
    }


    public static void compareAnswers(String s) {
        int optimalAnswer = findSmallestDivisor(s);
        int slowAnswer = findSmallestDivisorSlow(s);
        System.out.printf("With '%s', optimal = %d, slow = %d\n", s, optimalAnswer, slowAnswer);
        Assert.assertEquals(slowAnswer, optimalAnswer);

    }

}
