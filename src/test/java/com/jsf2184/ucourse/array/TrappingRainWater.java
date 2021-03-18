package com.jsf2184.ucourse.array;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.Stack;

@Slf4j
// https://leetcode.com/problems/trapping-rain-water/
public class TrappingRainWater {

    @Test
    public void test() {
        validate(new int[] {3,2,1}, 0);
        validate(new int[] {1,2,3,2,1}, 0);

        validate(new int[] {0,1,0,2,1,0,1,3,2,1,2,1}, 6);
        validate(new int[] {4,2,0,3,2,5}, 9);
    }
    public void validate(int[] height, int expected) {
        final int actual = trap(height);
        Assert.assertEquals(expected, actual);
        log.info("");
    }

    public int trap(int[] height) {
        int size = height.length;
        if (size <= 2) {
            return 0;
        }

        int water = 0;
        int lPtr = 0;
        int rPtr = size - 1;
        int lMax = height[lPtr];
        int rMax = height[rPtr];

        while (lPtr < rPtr) {
            if (lMax <= rMax) {
                int current = height[lPtr];
                if (current <= lMax) {
                    water += lMax - current;
                    lPtr++;
                } else {
                    lMax = current;
                }

            } else {
                int current = height[rPtr];
                if (current <= rMax) {
                    water += rMax - current;
                    rPtr--;
                } else {
                    rMax = current;
                }
            }
        }
        return water;
    }

    public int oldTrap(int[] height) {
        int size =height.length;
        int maxHeight = 0;
        Stack<Integer> rightHeightChanges = new Stack<>();

        for (int h=size-1; h>=0; h--) {
            final int curHeight = height[h];
            if (curHeight >= maxHeight) {
                rightHeightChanges.push(h);
                maxHeight = curHeight;
            }
        }

        int leftHeight = 0;
        int rightHeight = 0;
        int rightTransition=-1;
        int volume = 0;
        for (int i=0; i< size; i++) {
            int curHeight = height[i];
            if (curHeight > leftHeight) {
                leftHeight = curHeight;
            }
            if (i > rightTransition && !rightHeightChanges.isEmpty()) {
                if (!rightHeightChanges.isEmpty()) {
                    rightTransition = rightHeightChanges.pop();
                    rightHeight = height[rightTransition];
                }
            }
            int containerHeight = Math.min(leftHeight, rightHeight);
            log.info("At i={}, leftHeight={}, rightHeight={}", i, leftHeight, rightHeight);
            if (curHeight < containerHeight) {
                volume += containerHeight - curHeight;
            }
        }
        return  volume;
    }

}
