package com.jsf2184.Codility.Lesson1;

import org.junit.Assert;
import org.junit.Test;

import java.util.stream.IntStream;

public class Lesson1BinaryGap {

    @Test
    public void test() {
        Assert.assertEquals(4, getLongestZeroSequence(529));
        Assert.assertEquals(2, getLongestZeroSequence(9));
        Assert.assertEquals(1, getLongestZeroSequence(20));
        Assert.assertEquals(0, getLongestZeroSequence(0b001000));
        Assert.assertEquals(4, getLongestZeroSequence(0b00100100001));
        Assert.assertEquals(6, getLongestZeroSequence(0b100000010010001));
    }
    public static int getLongestZeroSequence(int N) {
        int mask = 1;
        Integer currentGapLength = null;
        int longestGap = 0;

        for (int i=0; i< Integer.SIZE; i++) {
            int bit = (N & mask) == 0 ? 0 : 1 ;
            if (bit == 1) {
                if (currentGapLength != null && currentGapLength > longestGap ) {
                    longestGap = currentGapLength;
                }
                currentGapLength = 0;
            } else if (currentGapLength != null){
                currentGapLength++;
            }
            mask = mask << 1;
        };
//        System.out.printf("Returning: %d \n", longestGap);
        return  longestGap;
    }

}
