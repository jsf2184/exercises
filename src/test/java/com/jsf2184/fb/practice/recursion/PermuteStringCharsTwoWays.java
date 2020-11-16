package com.jsf2184.fb.practice.recursion;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PermuteStringCharsTwoWays {

    @Test
    public void testIt() {
        System.out.println("ResultsWithoutPfx");
        ArrayList<String> resultsWithoutPfx = new ArrayList<>();
        permuteWithoutPrefix("abcd", resultsWithoutPfx);
        resultsWithoutPfx.forEach(System.out::println);

        System.out.println("");

        System.out.println("ResultsWithPfx");
        ArrayList<String> resultsWithPfx = new ArrayList<>();
        permuteWithPrefix("", "abcd", resultsWithPfx);
        resultsWithPfx.forEach(System.out::println);
        Assert.assertEquals(resultsWithoutPfx, resultsWithPfx);

    }
    public void permuteWithoutPrefix(String s, List<String> results) {
        if (s.length() == 1) {
            results.add(s);
        }
        for (int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            String remainder = s.substring(0, i) + s.substring(i+1);
            int priorSize = results.size();
            permuteWithoutPrefix(remainder, results);
            for (int p=priorSize; p<results.size(); p++) {
                results.set(p, c + results.get(p));
            }
        }
    }

    public void permuteWithPrefix(String pfx, String s, List<String> results) {
        if (s.length() == 1) {
            results.add(pfx + s);
        }
        for (int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            String remainder = s.substring(0, i) + s.substring(i+1);
            permuteWithPrefix(pfx+c, remainder, results);
        }
    }

}
