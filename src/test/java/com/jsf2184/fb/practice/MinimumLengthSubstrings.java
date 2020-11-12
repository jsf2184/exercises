package com.jsf2184.fb.practice;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MinimumLengthSubstrings {

    @Test
    public void testExample() {
        Assert.assertEquals(5, minLengthSubstring("dcbefebce", "fd"));
        Assert.assertEquals(2, minLengthSubstring("dcbefebcedf", "fd"));

    }

    public static class KeyChar {
        public int index;
        public char letter;

        public KeyChar(int index, char letter) {
            this.index = index;
            this.letter = letter;
        }
    }
    public static int minLengthSubstring(String s, String t) {
        // Write your code here
        int tLen = t.length();
        if (tLen > s.length()) {
            return -1;
        }

        Map<Character, Integer> tContents = new HashMap<>();
        t.chars()
         .forEach(c -> {
             final Integer count = tContents.computeIfAbsent((char) c, ch -> 0);
             tContents.put((char) c, count + 1);
         });


        ArrayList<KeyChar> keyChars = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (tContents.containsKey(c)) {
                keyChars.add(new KeyChar(i, c));
            }
        }

        Integer smallest = null;
        for (int start = 0; start < keyChars.size(); start++) {
            Map<Character, Integer> requirements = new HashMap<>(tContents);
            final Integer cost = calcCost(keyChars, start, requirements);
            if (cost == null) {
                if (smallest == null) {
                    return -1;
                }
                return smallest;
            }

            if (smallest == null || cost < smallest) {
                smallest = cost;
            }
        }
        return  smallest == null? -1 : smallest;
    }


    public static Integer calcCost(ArrayList<KeyChar> keyChars, int start, Map<Character, Integer> tContents) {
        int cost = 0;
        Integer lastKeyCharIndex = null;

        for (int i=start; i< keyChars.size(); i++) {
            KeyChar keyChar = keyChars.get(i);
            Integer charCount = tContents.get(keyChar.letter);
            if (charCount != null) {
                charCount = charCount-1;
                // decrement the count or if all requirements satisfied, remove the entry from the map.
                if (charCount == 0) {
                    tContents.remove(keyChar.letter);
                } else {
                    tContents.put(keyChar.letter, charCount);
                }
            }
            if (tContents.size() == 0) {
                lastKeyCharIndex = i;
                break;
            }
        }
        if (lastKeyCharIndex == null) {
            return null;
        }
        int result = (keyChars.get(lastKeyCharIndex).index - keyChars.get(start).index) + 1;
        return result;
    }

}
