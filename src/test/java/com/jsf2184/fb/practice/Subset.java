package com.jsf2184.fb.practice;

import org.junit.Test;

import java.util.ArrayList;

public class Subset {

    @Test
    public void testIt() {
        subset("abcd");
    }
    @Test
    public void testSubset2() {
        subset2("abcd");
    }

    public void subset(String s) {
        ArrayList<String> list = new ArrayList<>();
        list.add("");
        for (int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            addToList(c, list);
        }
        for (String subset : list) {
            System.out.println(subset);
        }
    }

    public void addToList(char c, ArrayList<String> list) {
        int len = list.size();
        for (int i=0; i<len; i++) {
            String s = list.get(i);
            list.add(c + s);
        }
    }

    public void subset2(String s) {
        ArrayList<String> results = new ArrayList<>();
        subset2(s, results);
        for (String subset : results) {
            System.out.println(subset);
        }

    }
    public void subset2(String s, ArrayList<String> results) {
        if (s.length() == 0) {
            results.add("");
            return;
        }
        char c = s.charAt(0);
        subset2(s.substring(1), results);
        addToList(c, results);
    }
}
