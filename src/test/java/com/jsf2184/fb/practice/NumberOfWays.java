package com.jsf2184.fb.practice;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class NumberOfWays {
//    Given a list of n integers arr[0..(n-1)], determine the number of different pairs of elements within it which sum to k.
//    If an integer appears in the list multiple times, each copy is considered to be different; that is, two pairs are considered
//    different if one pair includes at least one array index which the other doesn't, even if they include the same values.
//

//    n = 5
//    k = 6
//    arr = [1, 2, 3, 4, 3]
//    output = 2
//    The valid pairs are 2+4 and 3+3.

    @Test
    public void test1() {
        int[] arr = {1, 2, 3, 4, 3};
        int k = 6;
        Assert.assertEquals(2, numberOfWays(arr, k));
    }

//    n = 5
//    k = 6
//    arr = [1, 5, 3, 3, 3]
//    output = 4

    @Test
    public void test2() {
        int[] arr = {1, 5, 3, 3, 3};
        int k = 6;
        Assert.assertEquals(4, numberOfWays(arr, k));

    }

    public static int numberOfWays(int[] arr, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int val : arr) {
            if (val < k) {
                int prevCount = map.computeIfAbsent(val, v -> 0);
                map.put(val, prevCount + 1);
            }
        }
        int result = 0;
        for (int val : map.keySet()) {
            int partner = k - val;
            if (val > partner) {
                continue;
            }
            Integer partnerCount = map.get(partner);
            if (partnerCount == null ) {
                continue;
            }
            int valCount = map.get(val);
            if (val == partner) {
                result += (valCount * (valCount-1))/2;
            } else {
                result += valCount * partnerCount;
            }
        }
        return result;

    }

}
