package com.jsf2184.ucourse.array;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WaterContainer {

//        Given n non-negative integers a1, a2, ..., an , where each represents a point at coordinate (i, ai).
//        n vertical lines are drawn such that the two endpoints of the line i is at (i, ai) and (i, 0).
//        Find two lines, which, together with the x-axis forms a container, such that the container contains the most water.
//
//        Notice that you may not slant the container.


    @Test
    public void test() {
        vfy(new int[] {1,8,6,2,5,4,8,3,7}, 49);
        vfy(new int[] {4, 8, 1, 2, 3, 9}, 32);
    }


    public static void vfy(int[] height, int expected) {
        final int actual = maxArea(height);
        Assert.assertEquals(expected, actual);
    }

    public static int oldMaxArea(int[] height) {
        final int numPoints = height.length;
        int bestArea = 0;

        List<Integer> newHighs = new ArrayList<>();
        int maxHeight = 0;
        for (int j=numPoints-1; j>=0; j--) {
            int h = height[j];
            if (h > maxHeight) {
                newHighs.add(j);
                maxHeight = h;
            }
        }

        int maxLeft = 0;
        for (int left=0; left<numPoints; left++) {
            int lHeight = height[left];

            if (lHeight > maxLeft) {
                maxLeft = lHeight;
                for (int right : newHighs) {
                    if (right <= left) {
                        break;
                    }
                    int recWidth = right-left;
                    int rHeight = height[right];
                    int recHeight = Math.min(lHeight, rHeight);
                    final int area = recWidth * recHeight;
                    if (area > bestArea) {
                        bestArea = area;
                    }
                }
            }
        }
        return bestArea;
    }


    public static int maxArea(int[] height) {
        int left = 0;
        int right = height.length-1;
        int maxArea = 0;

        while (left < right) {
            int leftHeight = height[left];
            int rightHeight = height[right];
            int minHeight;
            int width = right - left;
            if (leftHeight < rightHeight) {
                minHeight = leftHeight;
                left++;
            } else {
                minHeight = rightHeight;
                right--;
            }
            maxArea = Math.max(maxArea, minHeight * width);
        }
        return maxArea;
    }
}
