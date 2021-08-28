package com.jsf2184.ucourse.string;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SmallestSubstringWithAllChars {

    /*
    Min Window Substring
    Have the function MinWindowSubstring(strArr) take the array of strings stored in strArr, which will contain only two strings,
    the first parameter being the string N and the second parameter being a string K of some characters, and your goal is to determine
    the smallest substring of N that contains all the characters in K. For example: if strArr is ["aaabaaddae", "aed"] then the
    smallest substring of N that contains the characters a, e, and d is "dae" located at the end of the string.
    So for this example your program should return the string dae.

    Another example: if strArr is ["aabdccdbcacd", "aad"] then the smallest substring of N that contains all of the characters
    in K is "aabd" which is located at the beginning of the string. Both parameters will be strings ranging in length
    from 1 to 50 characters and all of K's characters will exist somewhere in the string N. Both strings will only contains
    lowercase alphabetic characters.

    Examples
    Input: new String[] {"ahffaksfajeeubsne", "jefaa"}
    Output: aksfaje

    Input: new String[] {"aaffhkksemckelloe", "fhea"}
    Output: affhkkse

   */

    @Test
    public void buildData() {

        // Input: new String[] {"ahffaksfajeeubsne", "jefaa"}
        runTest("ahffaksfajeeubsne", "jefaa","aksfaje");


        // Input: new String[] {"aaffhkksemckelloe", "fhea"}
        runTest("aaffhkksemckelloe", "fhea","affhkkse");


        // Input: new String[] {"aaabaaddae", "aed"}
        runTest("aaabaaddae", "aed","dae");

        // Input: new String[] {"aabdccdbcacd", "aad"}
        runTest("aabdccdbcacd", "aad","aabd");


        /*
            the smallest substring of N that contains all the characters in K. For example: if strArr is ["aaabaaddae", "aed"] then the
    smallest substring of N that contains the characters a, e, and d is "dae" located at the end of the string.
    So for this example your program should return the string dae.

    Another example: if strArr is ["aabdccdbcacd", "aad"] then the smallest substring of N that contains all of the characters
    in K is "aabd" which is located at the beginning of the string. Both parameters will be strings ranging in length

         */
    }

    @Data
    public static class CharRecord {
        int desired;
        int actual;

        public CharRecord() {
            desired = 0;
            actual = 0;
        }

        public void incDesired() {
            desired++;
        }

        public void incActual() {
            actual++;
        }

        public boolean isSatisfied() {
            return actual >= desired;
        }

        public boolean decIfStillSatisfied() {
            if (actual - 1 >= desired) {
                // safe to remove this character
                actual--;
                return true;
            }
            return false;
        }

    }
    public static class Solution {
        Map<Character, CharRecord> desiredChars;
        int deficientCount;
        String N;
        Integer start;
        String best;

        public Solution(String N, String K) {
            this.N = N;
            desiredChars = new HashMap<>();
            for (int i=0; i<K.length(); i++) {
                char c = K.charAt(i);
                final CharRecord charRecord = desiredChars.computeIfAbsent(c, ch -> new CharRecord());
                charRecord.incDesired();
            }
            deficientCount = desiredChars.keySet().size();
            start = null;
            best = null;
        }

        String solve() {
            for (int i = 0; i<N.length(); i++) {
                char c = N.charAt(i);
                final CharRecord charRecord = desiredChars.get(c);
                if (charRecord == null) {
                    continue;
                }
                if (start == null) {
                    start = i;
                }

                boolean charWasSatisfied = charRecord.isSatisfied();
                charRecord.incActual();
                boolean charIsSatsified = charRecord.isSatisfied();
                boolean wasDeficient = deficientCount > 0;
                if (wasDeficient && (!charWasSatisfied && charIsSatsified)) {
                    // transitioned from a deficient char to a satisfied char.
                    deficientCount--;
                }
                boolean isDeficient = deficientCount > 0;
                log.info("N[{}]={} deficientCount={}, wasDeficient={}, isDeficient={}, ", i, c, deficientCount, wasDeficient, isDeficient);
                compress(i);
                if (!isDeficient) {
                    final int endIndex = i + 1;
                    int newLen = endIndex-start;
                    if (best == null || newLen < best.length()) {
                        best = N.substring(start, endIndex);
                        log.info("Current solution is best so far: start={}, finish={}, best={}", start, i, best);
                    }
                }
            }
            return best;
        }

        private boolean compress(int end) {
            int i;
            for (i=start; i<end; i++) {
                char c = N.charAt(i);
                final CharRecord charRecord = desiredChars.get(c);
                if (charRecord == null) {
                    // this was not a desired character.
                    continue;
                }
                if (!charRecord.decIfStillSatisfied()) {
                    break;
                }
            }
            if (i > start) {
                start = i;
                return true;
            }
            return false;
        }
    }



    public void runTest(String N, String K, String expected) {
        Solution solution = new Solution(N, K);
        log.info("\nFor, N={}, K={}: Initial deficientCount = {}", N, K, solution.deficientCount);
        final String result = solution.solve();
        log.info("result = {}", result);
        Assert.assertEquals(expected, result);

    }
}
