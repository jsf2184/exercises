package com.jsf2184.mlm;

import org.apache.commons.lang3.mutable.MutableInt;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Consumer;

public class MergeSort {

    public <T> T getNext(Iterator<T> iterator) {
        return iterator.hasNext() ? iterator.next() : null;
    }
    public <T extends Comparable<T>> void merge(Iterator<T> left, Iterator<T> right, Consumer<T> dest) {

        T lVal = getNext(left);
        T rVal = getNext(right);

        while(lVal != null || rVal != null) {

            T next;
            if (lVal != null && rVal != null) {
                if (lVal.compareTo(rVal) < 0) {
                    next = lVal;
                    lVal = getNext(left);
                } else {
                    next = rVal;
                    rVal = getNext(right);
                }
            } else if (lVal != null) {
                next = lVal;
                lVal = getNext(left);
            } else {
                next = rVal;
                rVal = getNext(right);
            }
            dest.accept(next);
        }
    }

    public <T extends Comparable<T>> void merge3(T[] left, T[] right, Consumer<T> dest) {
        int lSize = left.length;
        int rSize = right.length;

        int l=0;
        int r=0;

        while(l< lSize || r < rSize) {
            T lVal = l<lSize ?  left[l] : null;
            T rVal = r<rSize ?  right[r] : null;

            T next;
            if (lVal != null && rVal != null) {
                if (lVal.compareTo(rVal) < 0) {
                    next = lVal;
                    l++;
                } else {
                    next = rVal;
                    r++;
                }
            } else if (lVal != null) {
                next = lVal;
                l++;
            } else {
                next = rVal;
                r++;
            }
            dest.accept(next);
        }
    }


    public <T extends Comparable<T>> void merge2(T[] left, T[] right, T[] dest) {
        int lSize = left.length;
        int rSize = right.length;

        int l=0;
        int r=0;
        int d=0;
        while(l< lSize || r < rSize) {
            T lVal = l<lSize ?  left[l] : null;
            T rVal = r<rSize ?  right[r] : null;

            T next;
            if (lVal != null && rVal != null) {
                if (lVal.compareTo(rVal) < 0) {
                    next = lVal;
                    l++;
                } else {
                    next = rVal;
                    r++;
                }
            } else if (lVal != null) {
                next = lVal;
                l++;
            } else {
                next = rVal;
                r++;
            }
            dest[d] = next;
            d++;
        }
    }

    @Test
    public void testMerge() {
        Integer[] left = new Integer[] {1, 4, 9, 12, 15};

        Integer[] right = new Integer[] {2, 4, 10, 12, 17, 19, 21};

        Integer[] dest = new Integer[left.length + right.length];
        final MutableInt destIdx = new MutableInt(0);

        merge(Arrays.stream(left).iterator(),
              Arrays.stream(right).iterator(),
              (v) -> dest[destIdx.getAndIncrement()] = v );
        System.out.printf("%s\n", Arrays.toString(dest));


    }
    @Test
    public void charPlay() {
        char c = '0' + 1;
        Assert.assertEquals(c, '1');
        int num = 12345;
        for (int i= num; i != 0; i = i/10) {
            int dig = i% 10;
            System.out.println(dig);
        }

        int[] arr = new int[] {0, 1, 2, 3};
        reverse(arr);
        System.out.printf("%s\n", Arrays.toString(arr) );
        arr = new int[]{0, 1, 2, 3, 4};
        reverse(arr);
        System.out.printf("%s\n", Arrays.toString(arr) );

    }

    public void reverse(int[] arr) {
        int last = arr.length - 1;
        for (int i=0; i < arr.length /2 ; i++) {
            int tmp = arr[i];
            arr[i] = arr[last - i];
            arr[last-i] = tmp;
        }
    }
}
