package com.jsf2184.cracking;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;

public class UniqueChars_1_1 {
    public static boolean all_unique_with_set(String s) {
        HashSet<Character> seen = new HashSet<>();
        for (int i = 0; i < s.length(); i++) {
            final char c = s.charAt(i);
            if (seen.contains(c)) {return false;}
            seen.add(c);
        }
        return true;
    }

    public static boolean all_unique_with_array(String s) {
        boolean[] seen = new boolean[256];
        for (int i = 0; i < s.length(); i++) {
            final char c = s.charAt(i);
            if (seen[c]) {return false;}
            seen[c] = true;
        }
        return true;
    }

    // Here we restrict to all lower case letters so 26 choices can be flagged with 32 bits
    public static boolean all_unique_with_bits(String s) throws Exception {

        int seen = 0;

        for (int i = 0; i < s.length(); i++) {
            final char c = s.charAt(i);
            final int idx = c - 'a';
            if (idx < 0 || idx > 26) {
                throw new Exception(String.format("Bad character: %c", c));
            }
            int mask = 1<<idx;
            if ((seen & mask) != 0) {return false;}
            seen |= mask;
        }
        return true;
    }


    public static boolean all_unique_no_ds(String s) {
        for (int i = 0; i < s.length(); i++) {
            final char c = s.charAt(i);
            for(int j=i+1; j < s.length(); j++) {
                if (c == s.charAt(j)) {return false;}
            }
        }
        return true;
    }


    @Test
    public void testCasesWithSet() {
        Assert.assertTrue(all_unique_with_set("abc"));
        Assert.assertTrue(all_unique_with_set(""));
        Assert.assertFalse(all_unique_with_set("abca"));
    }

    @Test
    public void testCasesWithBits() throws Exception{
        Assert.assertTrue(all_unique_with_bits("abc"));
        Assert.assertTrue(all_unique_with_bits(""));
        Assert.assertFalse(all_unique_with_bits("abca"));

        boolean caught = false;
        try {
            all_unique_with_bits("abcA");
        } catch (Exception e) {
            caught = true;
        }
        Assert.assertTrue(caught);

    }

    @Test
    public void testCasesWithArray() {
        Assert.assertTrue(all_unique_with_array("abc"));
        Assert.assertTrue(all_unique_with_array(""));
        Assert.assertFalse(all_unique_with_array("abca"));
    }

    @Test
    public void testNoSetCases() {
        Assert.assertTrue(all_unique_no_ds("abc"));
        Assert.assertTrue(all_unique_no_ds(""));
        Assert.assertFalse(all_unique_no_ds("abca"));
    }

}
