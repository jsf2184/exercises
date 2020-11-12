package com.jsf2184.hackerrank.ice;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class P2 {

    @Test
    public void test() {
        compare("aaabbb");
    }

    public static void compare(String s) {
        Assert.assertEquals(substringCalculator(s), substringCalculator2(s));
    }

    public static long substringCalculator(String s) {
        // Write your code here
        HashSet<String> set = new HashSet<>();
        int length = s.length();
        for (int start = 0; start< length; start++) {
            int maxSubLen = length - start;
            for (int subLen = 1; subLen<= maxSubLen; subLen++) {
                String substring = s.substring(start, start+subLen);
                set.add(substring);
            }
        }
        System.out.println(set);
        return set.size();
    }

    public static long substringCalculator2(String s) {
        // Write your code here
        int length = s.length();
        int total = 0;
        for (int subLen = 1; subLen<= length; subLen++) {

            HashSet<String> set = new HashSet<>();

            int lastStart = length - subLen;
//            for (int start = 0; start< length; start++) {

            for (int start = 0; start <= lastStart; start++) {
                String substring = s.substring(start, start+subLen);
                set.add(substring);
            }
            total += set.size();
        }
        return total;
    }



}
