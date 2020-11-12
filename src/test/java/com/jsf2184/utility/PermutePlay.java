package com.jsf2184.utility;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

public class PermutePlay {

    public static String pullOne(int index, String input) {
        final int length = input == null ? 0 : input.length();
        if (index >= length) {
            return null;
        }
        String pre = input.substring(0, index);

        String post = "";
        if (index + 1 < length) {
            post = input.substring(index+1);
        }
        String result = pre+post;
        return result;
    }

    @Test
    public void testPullOne() {
        Assert.assertEquals("bc", pullOne(0, "abc"));
        Assert.assertEquals("ac", pullOne(1, "abc"));
        Assert.assertEquals("ab", pullOne(2, "abc"));
        Assert.assertEquals("b", pullOne(0, "ab"));
        Assert.assertEquals("a", pullOne(1, "ab"));
        Assert.assertEquals("", pullOne(0, "a"));
        Assert.assertNull(pullOne(0, ""));
    }
    @Test
    public void permuteAbc() {
        System.out.println("Recursive solution");
        final List<String> recursiveResults = permute("abcd");
        printSolution(recursiveResults);
        final List<String> stackResults = permuteWithStack("abcd");
        System.out.println("Stack solution");
        printSolution(stackResults);
        Assert.assertEquals(new HashSet<>(recursiveResults), new HashSet<>(stackResults));

    }

    public List<String>  permute(String str) {
        List<String> results = new ArrayList<>();
        permute("", str, results);
        return results;
    }

    public void printSolution(List<String> results) {
        for(int i=0; i<results.size(); i++) {
            System.out.printf("[%02d]: %s\n", i, results.get(i));
        }

    }


    public void permute(String front, String back, List<String> results) {
        final int backLength = back.length();
        if (backLength == 1) {
            results.add(front+back);
            return;
        }
        for (int i=0; i<backLength; i++) {
            char c =  back.charAt(i);
            String newFront = front+c;
            String newBack = pullOne(i, back);
            permute(newFront, newBack, results);
        }
    }

    public List<String> permuteWithStack(String s) {
        List<String> result = new ArrayList<>();
        if (s == null || s.length() == 0) {
            return result;
        }
        Entry entry = new Entry("", s);
        Stack<Entry> stack = new Stack<>();
        stack.push(entry);
        while(!stack.isEmpty()) {
            entry = stack.pop();
            entry.process(stack, result);
        }
        return result;
    }
    public static class Entry {
        String front;
        String back;

        public Entry(String front, String back) {
            this.front = front;
            this.back = back;
        }

        void process(Stack<Entry> stack, List<String> result) {
            final int backLength = back.length();
            if (backLength == 1) {
                String value = front + back;
                result.add(value);
            } else {
                for (int i = 0; i< backLength; i++) {
                    char c = back.charAt(i);
                    String newFront = front + c;
                    String newBack = pullOne(i, back);
                    stack.push(new Entry(newFront, newBack));
                }
            }
        }
    }



}
