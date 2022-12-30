package com.jsf2184.cracking;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class Urlify_1_3 {
    public static char[] urlify(String s) {
        int srcLen = s.length();
        final long numSpaces = s.chars().filter(c -> c == ' ').count();
        int reqSpace = (int) ((srcLen - numSpaces) + 3 * numSpaces);
        char[] res = new char[reqSpace];
        for (int i=0; i< srcLen; i++) { res[i] = s.charAt(i); }

//        int dest = reqSpace-1;
//        int src = srcLen - 1;

        for(int src = srcLen-1, dest=reqSpace-1; src >=0; src--) {
            char c = res[src];
            if (c != ' ') {
                res[dest--] = c;
            } else {
                res[dest--] = '0';
                res[dest--] = '2';
                res[dest--] = '%';
            }
        }
        return res;
    }

    @Test
    public void someTests() {
//        Assert.assertArrayEquals("abc".toCharArray(), urlify("abc"));
        Assert.assertArrayEquals("a%20b%20c".toCharArray(), urlify("a b c"));
//        Assert.assertArrayEquals("%2020".toCharArray(), urlify("  "));
    }
}
