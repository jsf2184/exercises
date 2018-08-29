package com.jsf2184;

import org.junit.Assert;
import org.junit.Test;

public class StringCompressTests {

    public static String compress(String input) {
        StringBuffer sb = new StringBuffer();

        for (int i=0; i<input.length();) {
            RepeatRes res = repeats(input, i);
            sb.append(res.toString());
            i += res.getFreq();
        }
        return sb.toString();

    }

    @Test
    public void testRepeats() {
        Assert.assertEquals("c3", repeats("abccc", 2));
        Assert.assertEquals("c1", repeats("abc", 2));
    }


    @Test
    public void testCompress() {
        Assert.assertEquals("", compress(""));
        Assert.assertEquals("a1b1c1", compress("abc"));
        Assert.assertEquals("a2b2c2d1", compress("aabbccd"));


    }
    public static class RepeatRes {
        char _c;
        int _freq;

        public RepeatRes(char c, int freq) {
            _c = c;
            _freq = freq;
        }

        public int getFreq() {
            return _freq;
        }

        @Override
        public String toString() {
            return "" + _c + _freq;
        }
    }
    public static RepeatRes repeats(String input, int i) {
        char c = input.charAt(i);
        int freq = 1;
        for (int j=i+1; j<input.length(); j++) {
            char next = input.charAt(j);
            if (next != c) {
                break;
            }
            freq++;
        }
        RepeatRes res = new RepeatRes(c, freq);
        return res;
    }

}
