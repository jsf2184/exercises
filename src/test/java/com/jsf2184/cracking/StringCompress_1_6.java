package com.jsf2184.cracking;

import org.junit.Assert;
import org.junit.Test;

public class StringCompress_1_6 {
    String compress(String src) {
        int i=0;
        StringBuilder sb = new StringBuilder();
        if (src.length() == 0) {
            return src;
        }
        while(i<src.length() ) {
            char c = src.charAt(i);
            int len = getRepeat(i, src);
            sb.append(String.format("%c%d", c, len));
            i+= len;
        }
        return sb.toString();
    }

    int getRepeat(int i, String src) {
        if (i >= src.length()) {
            return 0;
        }
        char c = src.charAt(i);
        int j;
        for(j=i+1; j<src.length(); j++) {
            if (src.charAt(j) != c) {
                break;
            }
        }
        return j-i;
    }

    @Test
    public void testRepeat() {
        Assert.assertEquals(0, getRepeat(0, ""));
        Assert.assertEquals(1, getRepeat(0, "a"));
        Assert.assertEquals(1, getRepeat(0, "abc"));
        Assert.assertEquals(2, getRepeat(0, "aabc"));
        Assert.assertEquals(3, getRepeat(0, "aaa"));
    }

    @Test
    public void testCompress() {
        Assert.assertEquals("a1b1c1", compress("abc"));
        Assert.assertEquals("a2b2c1d4", compress("aabbcdddd"));
        Assert.assertEquals("", compress(""));
    }

}
