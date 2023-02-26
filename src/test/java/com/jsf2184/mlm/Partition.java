package com.jsf2184.mlm;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

// A little exercise to write qsort partition method
public class Partition {
    // start and end are inclusive
    public static int partition(int start, int end, int[] array) {
        if (end <= start) {
            return start;
        }
        int pivot = array[end];
        int numSmaller = 0;
        for(int i=start; i<end; i++) {
            if (array[i] < pivot) {
                swap(start + numSmaller, i, array);
                numSmaller++;
            }
        }
        swap(start + numSmaller, end, array);
        return start+ numSmaller;
    }
    public static void swap(int i, int j, int[] array) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    @Test
    public void testCompare() {
        Assert.assertFalse(compare(0, new int[]{1, 2, 3, 4}, new int[]{2,3,4}));
        Assert.assertTrue(compare(1, new int[]{1, 2, 3, 4}, new int[]{2,3,4}));
    }

    @Test
    public void testPartition() {

        vfyPartition(2,6, new int[] {10,9,8,3,5,2,4});

    }

    public static void vfyPartition(int start, int end, int[] arr) {
        int pval = arr[end];
        System.out.printf("pval=%d, input: %s\n", pval, formatArray(arr));

        int p = partition(start, end, arr);

        System.out.printf("pval=%d, p=%d, adj_p=%d, subArray: %s\n", pval, p, p-start, formatArray(start, end, arr));
        Assert.assertEquals(pval, arr[p]);
        for(int i=start; i<=end; i++) {
            if (i < p) {
                Assert.assertTrue(arr[i] <= pval);
            } else if (i > p) {
                Assert.assertTrue(arr[i] >= pval);
            }
        }
    }

    public static String formatArray(int[] arr) {
        return formatArray(0, arr.length-1, arr);
    }

    public static String formatArray(int start, int end, int[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i=start; i<=end; i++) {
            if (i > start) {
                sb.append(", ");
            }
            sb.append(arr[i]);
        }
        return sb.toString();
    }
    public static boolean compare(int start, int[] array, int[] expected) {
        int[] subarray = ArrayUtils.subarray(array, start, start + expected.length);
        return Arrays.equals(expected, subarray);
    }

    public void sort(int start, int end, int[] arr) {
        if (end <= start) {
            return;
        }
        int p = p2(start, end, arr);
        sort(start, p-1, arr);
        sort(p+1, end, arr);
    }
    public int p2(int start, int end, int[] arr) {
        if (end <= start){
            return start;
        }
        int pivot = arr[end];
        int numLess = 0;
        for (int i=start; i<end; i++) {
            int v = arr[i];
            if (v < pivot) {
                swap(i, start+numLess, arr);
                numLess++;
            }
        }
        int res = start+numLess;
        swap(res, end, arr);
        return res;
    }
    @Test
    public void testSort() {
        int[] arr = {2, 7, 1, 3, 4, 6, 4, 5};
        sort(0, arr.length-1, arr);
        System.out.println(formatArray(arr));
    }

    public static String interleave(String s1, String s2) {
        String largest = s1;
        String smallest = s2;

        if (s2.length() > s1.length()) {
            largest = s2;
            smallest = s1;
        }

        int smallSize = smallest.length();
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<smallSize; i++) {
            sb.append(s1.charAt(i));
            sb.append(s2.charAt(i));
        }
        if (largest.length() > smallSize) {
            sb.append(largest.substring(smallSize));
        }
        return sb.toString();
    }

    @Test
    public void interleaveTest() {
        Assert.assertEquals(interleave("abc", "123"), "a1b2c3");
        Assert.assertEquals(interleave("abcdef", "123"), "a1b2c3def");
        Assert.assertEquals(interleave("abc", "123456"), "a1b2c3456");
        Assert.assertEquals(interleave("abc", ""), "abc");
        Assert.assertEquals(interleave("", "123"), "123");

    }

}
