package com.jsf2184.ucourse.array;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

// https://leetcode.com/problems/two-sum/
public class TwoSum {

    @Test
    public void test() {
        testIt(new int[] {3,7}, 9, null);
        testIt(new int[] {3}, 3, null);
        testIt(new int[] {}, 9, null);
        testIt(new int[] {5,3, 5}, 10, new int[] {0, 2});
        testIt(new int[] {5,3, 2, 4, 9 }, 12, new int[] {1, 4});
    }


    public int[] twoSumImproved(int[] nums, int target) {
        Map<Integer,Integer> map = new HashMap<>();

        for (int i=0; i< nums.length; i++) {
            int n = nums[i];
            final Integer other = map.get(n);
            if (other != null) {
                return new int[] {other, i};
            }
            int desired = target -n;
            map.put(desired, i);
        }

        return null;
    }

    public int[] twoSumImproved2(int[] nums, int target) {
        Map<Integer,Integer> map = new HashMap<>();

        for (int i=0; i< nums.length; i++) {
            int n = nums[i];
            int desired = target -n;
            final Integer other = map.get(desired);
            if (other != null) {
                return new int[] {other, i};
            }
            map.put(n, i);
        }

        return null;
    }


    public void testIt(int[] nums, int target, int[] expected) {
        final int[] actual = twoSumImproved2(nums, target);
        Assert.assertArrayEquals(expected, actual);
    }

}
