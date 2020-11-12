package com.jsf2184.fb.practice;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class MatchingPairs {

    @Test
    public void testIt() {
        testOne("abcd", "adcb", 4);
        testOne("abc", "abc", 1);
        testOne("abcc", "abcc", 4);
        testOne("abx", "adb", 2);
        testOne("abx", "abd", 1);
        testOne("abxa", "abda", 3);
    }

    public void testOne(String s, String t, int expected) {
        Assert.assertEquals(expected, matchingPairs(s, t));

    }
    public static int matchingPairs(String s, String t) {
        // Count the original number of matches
        Set<Character> charsSeen = new HashSet<>();
        boolean dupSChar = false;
        int originalMatches = 0;
        for (int i=0; i< s.length(); i++) {
            char c = s.charAt(i);
            if (!dupSChar) {
              if (charsSeen.contains(c)) {
                  dupSChar = true;
              } else {
                  charsSeen.add(c);
              }
            }
            if (c == t.charAt(i)) {
                originalMatches++;
            }
        }

        // A few special cases.
        if (originalMatches == s.length()) {
            if (dupSChar) {
                // we could just swap the 2 matches without hurting us
                return originalMatches;
            }
            // no matches, we'll lose 2 with the switch
            return originalMatches - 2;
        }

        if (originalMatches == s.length() - 1) {
            // we matched on all but one. Note a switch can't possibly help us but it could hurt us.
            if (dupSChar) {
                // we could just swap the 2 matches without hurting us
                return originalMatches;
            }
            // if we were to switch the one that didn't match with any other, we'd be one worse
            return originalMatches - 1;
        }
        int bestAdvantage = -2;
        for (int i=0; i< s.length(); i++) {
            int matchAtI = s.charAt(i) == t.charAt(i) ? 1 : 0;
            if (matchAtI == 0) {
                for (int j = i + 1; j < s.length(); j++) {
                    int matchAtJ = s.charAt(j) == t.charAt(j) ? 1 : 0;
                    int noSwitchMatches = matchAtI + matchAtJ;
                    int newMatchAtI = s.charAt(j) == t.charAt(i) ? 1 : 0;
                    int newMatchAtJ = s.charAt(i) == t.charAt(j) ? 1 : 0;
                    int withSwitchMatches = newMatchAtI + newMatchAtJ;
                    int advantage = withSwitchMatches - noSwitchMatches;
                    if (advantage == 2) {
                        return originalMatches + 2;
                    }
                    bestAdvantage = Math.max(bestAdvantage, advantage);
                }
            }
        }
        return originalMatches + bestAdvantage;
    }

}
