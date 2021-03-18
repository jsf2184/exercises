package com.jsf2184.wf;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.bytebuddy.implementation.bind.ArgumentTypeResolver;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.*;

public class LongestCommonUrlSequence {


/*
We have some clickstream data that we gathered on our client's website. Using cookies, we collected snippets of users' anonymized URL histories while they browsed the site. The histories are in chronological order, and no URL was visited more than once per person.

Write a function that takes two users' browsing histories as input and returns the longest contiguous sequence of URLs that appears in both.

Sample input:

user0 = ["/start", "/green", "/blue", "/pink", "/register", "/orange", "/one/two"]
user1 = ["/start", "/pink", "/register", "/orange", "/red", "a"]
user2 = ["a", "/one", "/two"]
user3 = ["/pink", "/orange", "/yellow", "/plum", "/blue", "/tan", "/red", "/amber", "/HotRodPink", "/CornflowerBlue", "/LightGoldenRodYellow", "/BritishRacingGreen"]
user4 = ["/pink", "/orange", "/amber", "/BritishRacingGreen", "/plum", "/blue", "/tan", "/red", "/lavender", "/HotRodPink", "/CornflowerBlue", "/LightGoldenRodYellow"]
user5 = ["a"]
user6 = ["/pink","/orange","/six","/plum","/seven","/tan","/red", "/amber"]

Sample output:

findContiguousHistory(user0, user1) => ["/pink", "/register", "/orange"]
findContiguousHistory(user0, user2) => [] (empty)
findContiguousHistory(user2, user1) => ["a"]
findContiguousHistory(user5, user2) => ["a"]
findContiguousHistory(user3, user4) => ["/plum", "/blue", "/tan", "/red"]
findContiguousHistory(user4, user3) => ["/plum", "/blue", "/tan", "/red"]
findContiguousHistory(user3, user6) => ["/tan", "/red", "/amber"]

n: length of the first user's browsing history
m: length of the second user's browsing history
 */



    static final String[] user0 = {"/start", "/green", "/blue", "/pink", "/register", "/orange", "/one/two"};
    static final String[] user1 = {"/start", "/pink", "/register", "/orange", "/red", "a"};
    static final String[] user2 = {"a", "/one", "/two"};
    static final String[] user3 = {"/pink", "/orange", "/yellow", "/plum", "/blue", "/tan", "/red", "/amber", "/HotRodPink", "/CornflowerBlue", "/LightGoldenRodYellow", "/BritishRacingGreen"};
    static final String[] user4 = {"/pink", "/orange", "/amber", "/BritishRacingGreen", "/plum", "/blue", "/tan", "/red", "/lavender", "/HotRodPink", "/CornflowerBlue", "/LightGoldenRodYellow"};
    static final String[] user5 = {"a"};
    static final String[] user6 = {"/pink","/orange","/six","/plum","/seven","/tan","/red", "/amber"};



    @Test
    public void testIt() {
        findContiguousHistory(user0, user1, Arrays.asList("/pink", "/register", "/orange"));
        findContiguousHistory(user3, user4, Arrays.asList("/plum", "/blue", "/tan", "/red"));
        findContiguousHistory(user0, user2, new ArrayList<>());

        findContiguousOptimized(user0, user1, Arrays.asList("/pink", "/register", "/orange"));
        findContiguousOptimized(user3, user4, Arrays.asList("/plum", "/blue", "/tan", "/red"));
        findContiguousOptimized(user0, user2, new ArrayList<>());

        findContiguousOptimized2(user0, user1, Arrays.asList("/pink", "/register", "/orange"));
        findContiguousOptimized2(user3, user4, Arrays.asList("/plum", "/blue", "/tan", "/red"));
        findContiguousOptimized2(user0, user2, new ArrayList<>());


    }

    public static List<String> findContiguousHistory(String[] u0, String[] u1, List<String> expected) {

        Pair bestPair = new Pair(-1, 0);
        for(int i=0; i<u0.length; i++) {
            Pair pair = longestSequenceStartingAt(u0, u1, i, bestPair.len);
            if (pair != null) {
                bestPair = pair;
            }
        }
        final List<String> maxList = getSubList(bestPair.start, bestPair.len, u0);
        System.out.printf("\nresult: %s\n", maxList.toString());
        Assert.assertEquals(expected, maxList);
        return maxList;
    }


    public static class Range {
        int start;
        int length;

        public Range(int start) {
            this.start = start;
            length = 1;
        }

        public void incLength() {
            length++;
        }

        public boolean isContiguous(Integer idx) {
            return  idx != null && idx == start + length;
        }
    }




    public static List<String> findContiguousOptimized2(String[] u0, String[] u1, List<String> expected) {

        Map<String, Integer> u0Words = new HashMap<>();
        for (int i = 0; i < u0.length; i++) {
            u0Words.put(u0[i], i);
        }
        Range currentRange = null;
        Range maxRange = null;

        for (int i = 0; i < u1.length; i++) {
            String word = u1[i];
            final Integer u0Idx = u0Words.get(word);
            if (currentRange == null) {
                if (u0Idx != null) {
                    currentRange = new Range(u0Idx);
                }
            } else if (currentRange.isContiguous(u0Idx)) {
                currentRange.incLength();

            } else {
                if (maxRange == null || currentRange.length > maxRange.length) {
                    maxRange = currentRange;
                }
                if (u0Idx != null) {
                    currentRange = new Range(u0Idx);
                } else {
                    currentRange = null;
                }
            }
        }
        if (currentRange != null &&  currentRange.length > maxRange.length) {
            maxRange = currentRange;
        }

        final List<String> maxList = maxRange == null ? new ArrayList<>() : getSubList(maxRange.start, maxRange.length, u0);

        System.out.printf("\nresult: %s\n", maxList.toString());
        Assert.assertEquals(expected, maxList);

        return maxList;
    }



    public static List<String> findContiguousOptimized(String[] u0, String[] u1, List<String> expected) {

        Map<String, Integer> u0Words = new HashMap<>();
        for (int i=0; i<u0.length; i++) {
            u0Words.put(u0[i], i);
        }
        Map<String, Integer> u1Starts = new HashMap<>();
        for (int i=0; i<u1.length; i++) {
            String word = u1[i];
            if (u0Words.containsKey(word)) {
                u1Starts.put(word, i);
            }
        }

        Pair bestPair = new Pair(-1, 0);
        for (Map.Entry<String, Integer> u1Entry :  u1Starts.entrySet()) {
            final String u1Word = u1Entry.getKey();
            final Integer u0Start = u0Words.get(u1Word);
            final int len = getSequence(u0Start, u0, u1Entry.getValue(), u1);
            if (len > bestPair.len) {
                bestPair = new Pair(u0Start, len);
            }
        }
        final List<String> maxList = getSubList(bestPair.start, bestPair.len, u0);
        Assert.assertEquals(expected, maxList);
        System.out.printf("\nresult: %s\n", maxList.toString());
        Assert.assertEquals(expected, maxList);
        return maxList;
    }



    @Data
    @AllArgsConstructor
    public static class Pair {
        int start;
        int len;
    }

    public static Pair longestSequenceStartingAt(String[] u0, String[] u1, int start0Index, int sizeToBeat) {
        final Integer u1Index = find(u0[start0Index], u1);
        if (u1Index == null) {
            return null;
        }
        final int len = getSequence(start0Index, u0, u1Index, u1);
        if (len > sizeToBeat) {
            return new Pair(start0Index, len);
        }
        return null;
    }

    static Integer find(String key, String[] arr) {
        for (int i=0; i< arr.length; i++) {
            if (key.equals(arr[i])) {
                return i;
            }
        }
        return null;
    }

    static int getSequence(int u0Idx, String[] u0, int u1Idx, String[] u1) {
        int len = 0;
        for (; u0Idx < u0.length && u1Idx < u1.length; u0Idx++, u1Idx++) {
            if (!u0[u0Idx].equals(u1[u1Idx])) {
                break;
            }
            len++;
        }
        return len;
    }

    static List<String> getSubList(int start, int len, String[] arr) {
        List<String > res = new ArrayList<>();
        for (int i=start; i < start+len; i++) {
            res.add(arr[i]);
        }
        return res;
    }

    public static List<String> longestSequenceStartingAt2(String[] u0, String[] u1, int start0Index) {
        int u1Index = 0;
        int u0Index = start0Index;

        String u0Word = u0[u0Index];
        List<String> result = new ArrayList<>();

        while (u1Index < u1.length) {
            String u1Word = u1[u1Index];
            while (u0Word.equals(u1Word)) {
                result.add(u0Word);
                u0Index++;
                u1Index++;
                if (u1Index >= u1.length || u0Index >= u0.length) {
                    break;
                }
                u1Word = u1[u1Index];
                u0Word = u0[u0Index];
            }
            if (result.size() > 0) {
                return result;
            }
            u1Index++;
        }

        return result;
    }






}

