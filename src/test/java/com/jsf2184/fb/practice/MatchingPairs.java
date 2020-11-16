package com.jsf2184.fb.practice;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class MatchingPairs {

    @Test
    public void testIt() {
        // improve by 2 case
        testOne("abcd", "adcb", 4);
        // match turns to loss of 2
        testOne("abc", "abc", 1);
        // match that doesn't lose ground
        testOne("abcc", "abcc", 4);
        // Improve by 1 where previously unmatched s matches a new t
        testOne("abx", "adb", 2);
        // Improve by 1 where previously unmatched t matches a new s
        testOne("axd", "adb", 2);
        // One unmatched turns into two.
        testOne("abx", "abd", 1);
        // One unmatched does not lose ground.
        testOne("abxa", "abda", 3);
        // no improvement, no harm
        testOne("abx", "acy", 1);
    }

    public void testOne(String s, String t, int expected) {
        Assert.assertEquals(expected, bestMatchingPairs(s, t));

    }

    public int bestMatchingPairs(String s, String t) {
        int matchCount = 0;

        for (int i=0; i<s.length(); i++) {
            char tChar = t.charAt(i);
            char sChar = s.charAt(i);
            if (sChar == tChar) {
                matchCount++;
            }
        }

        // For this case, any flip will cost us 2
        //   abc
        //   abc
        // For this case, we can do a flip at no cost because there are duplicates
        //   aba
        //   aba

        if (matchCount == s.length()) {
            // every single char matched right from the start.
            return anyDuplicates(s) ? matchCount : matchCount - 2;
        }

        // For this case, fliping a match with a non-match will cost us 1
        //   abc
        //   abd
        // For this case, we can do a flip at no cost because there are duplicates
        //   add
        //   adc

        if (matchCount == s.length() - 1) {
            return anyDuplicates(s) ? matchCount : matchCount - 1;
        }

        // Can we improve by 2?
        if (opportunitiesToImproveBy2(s, t)) {
            return matchCount + 2;
        }


        // Can we improve by 1?
        if (opportunitiesToImproveBy1(s, t)) {
            return matchCount + 1;
        }

        return matchCount;
    }

    private boolean opportunitiesToImproveBy1(String s, String t) {
        Set<Character> unmatchedTChars = new HashSet<>();
        Set<Character> unmatchedSChars = new HashSet<>();

        for (int i = 0; i < s.length(); i++) {
            char tChar = t.charAt(i);
            char sChar = s.charAt(i);
            if (sChar != tChar) {

                // The new sChar could match a previously unmatched tChar
                // s: ab
                // t: bx
                if (unmatchedTChars.contains(sChar)) {
                    return true;
                }
                unmatchedTChars.add(tChar);

                // The new tChar could match a previously unmatched sChar.
                // s: ba
                // t: xb
                unmatchedSChars.add(sChar);
                if (unmatchedSChars.contains(tChar)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean opportunitiesToImproveBy2(String s, String t) {
        HashSet<String> opportunities = new HashSet<>();

        for (int i=0; i<s.length(); i++) {
            char tChar = t.charAt(i);
            char sChar = s.charAt(i);
            if (sChar != tChar) {
                String pair = "" + tChar +  sChar;
                String desired = "" + sChar + tChar;
                if (opportunities.contains(desired)) {
                    return true;
                }
                opportunities.add(pair);
            }
        }
        return false;
    }

    boolean anyDuplicates(String s) {
        Set<Character> charsSeen = new HashSet<>();
        for (int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            if (charsSeen.contains(c)) {
                return true;
            }
            charsSeen.add(c);
        }
        return false;
    }


}
