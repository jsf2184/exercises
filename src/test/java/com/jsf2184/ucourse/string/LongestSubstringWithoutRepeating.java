package com.jsf2184.ucourse.string;

import org.junit.Assert;
import org.junit.Test;

// https://leetcode.com/problems/longest-substring-without-repeating-characters/
public class LongestSubstringWithoutRepeating {

    @Test
    public void testIt()
    {
        Assert.assertEquals(4, lengthOfLongestSubstring("pwwkewz"));
        Assert.assertEquals(3, lengthOfLongestSubstring("abcabcbb"));
        Assert.assertEquals(1, lengthOfLongestSubstring("bbbbb"));
        Assert.assertEquals(3, lengthOfLongestSubstring("pwwkew"));
    }

    public int lengthOfLongestSubstring(String s) {
        Integer[] seen = new Integer[256];
        int i=0;
        int longest = 0;
        int start = 0;
        while (i < s.length()) {
            char c = s.charAt(i);
            Integer prevIdx = seen[c];
            if (prevIdx != null) {
                // saw a repeat
                int newStart = prevIdx + 1;
                for (int j = start; j < newStart; j++) {
                    seen[s.charAt(j)] = null;
                }
                longest = Math.max(longest, i - start);
                start = newStart;
            }
            seen[c] = i;
            i++;

        }
        longest = Math.max(longest,i-start);
        return longest;
    }

    public int lengthOfLongestSubstring1(String s) {
        Integer[] seen = new Integer[256];
        int i=0;
        int longest = 0;
        int start = 0;
        while (i < s.length()) {
            char c = s.charAt(i);
            Integer prevIdx = seen[c];
            if (prevIdx == null) {
                seen[c] = i;
                i++;
                continue;
            }
            // saw a repeat
            int newStart = prevIdx+1;
            for (int j=start; j< newStart; j++) {
                seen[s.charAt(j)] = null;
            }
            longest = Math.max(longest, i-start);
            seen[c] = i;
            i++;
            start = newStart;

        }
        longest = Math.max(longest,i-start);
        return longest;
    }


    public int lengthOfLongestSubstring2(String s) {
        Integer[] seen = new Integer[256];
        int i=0;
        int longest = 0;
        int curLength = 0;
        while (i < s.length()) {
            char c = s.charAt(i);
            Integer prevIdx = seen[c];
            if (prevIdx == null) {
                seen[c] = i;
                curLength++;
                i++;
                continue;
            }
            // saw a repeat
            longest = Math.max(longest, curLength);
            i = prevIdx+1;
            curLength = 0;
            seen = new Integer[256];
        }
        longest = Math.max(longest,curLength);
        return longest;
    }

}
