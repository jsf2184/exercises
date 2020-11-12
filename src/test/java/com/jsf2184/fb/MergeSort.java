package com.jsf2184.fb;

import org.junit.Assert;
import org.junit.Test;

public class MergeSort {

    public static int[] mergeSort(int[] arr) {
        return mergeSort(arr, 0, arr.length);
    }

    public static int[] mergeSort(int[] arr, int start, int len) {
        if (len == 1) {
            return  new int[] {arr[start]};
        }
        int len0 = len/2;
        int len1= len -len0;
        int[] arr0 = mergeSort(arr, start, len0);
        int[] arr1 = mergeSort(arr, start+len0, len1);
        int[] result = merge(arr0, arr1);
        return result;
    }


    public void qsort(int[] arr, int start, int end) {
        if (start >= end) {
            return;
        }
        // suppose start = 10, len=5,
        int p = partition(arr, start, end);
        qsort(arr, start, p-1);
        qsort(arr, p+1, end);
    }


    // return the idx of the partition point
    public static int partition(int[] arr, int start, int end) {
        int pval= arr[end];
        int smallIdx = start;
        for (int i=start;  i<end; i++) {
            if (arr[i] < pval) {
                swap(arr, i, smallIdx);
                smallIdx++;
            }
        }
        swap(arr, smallIdx, end);
        return smallIdx;
    }

    // i = 0
    // end = 4
    // smallIdx=0
    // 9, 4, 2, 10, 5

    // i=1
    // smallIdx=0
    // swap(0, 1)
    // 4, 9, 2, 10, 5
    // smallIdx=1
    // i = 2

    // 4, 9, 2, 10, 5
    // swap(1, 2)
    // 4, 2, 9, 10, 5
    // smallIdx = 2
    // i=3
    // i=4=end
    // swap(4, 2)
    // 4, 2, 5, 10, 9



    public static void swap(int[] arr, int idx1, int idx2) {

    }


    public static class ArrFetcher {
        int[] arr;
        int idx;

        public ArrFetcher(int[] arr) {
            this.arr = arr;
            idx = 0;
        }
        Integer peek() {
            if (idx < arr.length) {
                return arr[idx];
            }
            return null;
        }
        void next() {
            idx++;
        }

    }
    public static int[] merge(int[] arr0, int[]arr1) {
        int len0= arr0.length;
        int len1= arr1.length;
        final int totalLen = len0 + len1;
        int[] result = new int[totalLen];
        ArrFetcher fetcher0 = new ArrFetcher(arr0);
        ArrFetcher fetcher1 = new ArrFetcher(arr1);
        for (int destIdx=0; destIdx < totalLen; destIdx++) {
            final Integer value0 = fetcher0.peek();
            final Integer value1 = fetcher1.peek();
            if (value0 != null && (value1 == null || value0 < value1)) {
                result[destIdx] = value0;
                fetcher0.next();
            } else {
                result[destIdx] = value1;
                fetcher1.next();
            }
        }
        return result;
    }

    @Test
    public void testMerge() {
        int[] arr1 = {0, 1, 2, 3, 8};
        int[] arr2 = {4, 5, 6, 7, 9};
        final int[] result = merge(arr1, arr2);
        for (int i=0; i< result.length; i++) {
            Assert.assertEquals(i, result[i]);
        }
        Assert.assertEquals(10, result.length);
    }
    @Test
    public void testMergeSort() {
        int[] arr1 = {4, 5, 6, 0, 1, 2, 7, 9, 3, 8};
        final int[] result = mergeSort(arr1);
        for (int i=0; i< result.length; i++) {
            Assert.assertEquals(i, result[i]);
        }
        Assert.assertEquals(10, result.length);
    }

}
