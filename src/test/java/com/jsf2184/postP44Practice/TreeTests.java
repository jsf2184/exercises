package com.jsf2184.postP44Practice;

import org.junit.Assert;
import org.junit.Test;
import sun.java2d.pipe.SpanClipRenderer;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class TreeTests {

    public static class RangeSpan {
        private int lowIdx;
        private int hiIdx;
        private int midIdx;

        public RangeSpan(int lowIdx, int hiIdx) {
            this.lowIdx = lowIdx;
            this.hiIdx = hiIdx;
            this.midIdx = lowIdx + (hiIdx - lowIdx) / 2;
        }

        public int getMidIdx() {
            return  midIdx;
        }

        public RangeSpan getLeftHalf() {
            if (midIdx <= lowIdx) {
                return null;
            }
            return  new RangeSpan(lowIdx, midIdx-1);
        }

        public RangeSpan getRightHalf() {
            if (midIdx >= hiIdx) {
                return null;
            }
            return  new RangeSpan(midIdx+1, hiIdx);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof RangeSpan)) return false;
            RangeSpan rangeSpan = (RangeSpan) o;
            return lowIdx == rangeSpan.lowIdx &&
                    hiIdx == rangeSpan.hiIdx &&
                    midIdx == rangeSpan.midIdx;
        }

        @Override
        public int hashCode() {
            return Objects.hash(lowIdx, hiIdx, midIdx);
        }
    }


    @Test
    public void testShortSpanOperations() {
        testSpan(new RangeSpan(0, 0),
                 0,
                 null,
                 null);
        testSpan(new RangeSpan(0, 1),
                 0,
                 null,
                 new RangeSpan(1, 1));

        testSpan(new RangeSpan(1, 1),
                 1,
                 null,
                 null);

        testSpan(new RangeSpan(0, 2),
                 1,
                 new RangeSpan(0, 0),
                 new RangeSpan(2, 2));


    }

    public void testSpan(RangeSpan rangeSpan,
                         int expectedMid,
                         RangeSpan expectedLeft,
                         RangeSpan expectedRight)
    {
        int actualMid = rangeSpan.getMidIdx();
        Assert.assertEquals(expectedMid, actualMid);
        RangeSpan leftHalf = rangeSpan.getLeftHalf();
        Objects.equals(expectedLeft, leftHalf);
        RangeSpan rightHalf = rangeSpan.getRightHalf();
        Objects.equals(expectedRight, rightHalf);
    }

    public static class TreeBuilder {

        public static void sequenceCalls(RangeSpan span, Consumer<Integer> consumer) {
            int midIdx = span.getMidIdx();
            consumer.accept(midIdx);
            RangeSpan leftHalf = span.getLeftHalf();
            if (leftHalf != null) {
                sequenceCalls(leftHalf, consumer);
            }
            RangeSpan rightHalf = span.getRightHalf();
            if (rightHalf != null) {
                sequenceCalls(rightHalf, consumer);
            }
        }
    }

    @Test
    public void testSequenceCalls() {
        testSequence(15);
        testSequence(16);
        testSequence(17);

    }

    public static void testSequence(int num) {
        RangeSpan rangeSpan = new RangeSpan(0, num-1);
        List<Integer> collected = new ArrayList<>();
        TreeBuilder.sequenceCalls(rangeSpan, collected::add);
        Assert.assertEquals(num, collected.size());
        IntStream.range(0, num).forEach(v -> Assert.assertTrue(collected.contains(v)));
        System.out.println(collected);
    }
}
