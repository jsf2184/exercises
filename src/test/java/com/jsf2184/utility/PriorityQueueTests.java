package com.jsf2184.utility;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.function.BiFunction;

public class PriorityQueueTests {
    private static final Logger _log = Logger.getLogger(PriorityQueueTests.class);

    @BeforeClass
    public static void setup()
    {
        LoggerUtility.initRootLogger();
    }

    @Test
    public void testPriorityQueue() {
        // Default priority Queue has the root be the smallest so the values are
        // stored in increasing order.
        popAndPrintPriorityQueue((x, y) -> x - y );
    }
    @Test
    public void testReversePriorityQueue() {
        // Reverse PriorityQueue has the root be the largest so the values are stored in decreasing
        // order.
        popAndPrintPriorityQueue((x, y) -> y - x );

    }

    public void popAndPrintPriorityQueue(Comparator<Integer> comparer )
    {
        PriorityQueue<Integer> sut = new PriorityQueue<>(comparer);
        int[] shuffle = Utility.shuffle(10);
        _log.info(String.format("input: %s", Arrays.toString(shuffle)));
        Arrays.stream(shuffle).boxed().forEach(sut::add);
        _log.info("Iterate thru priorityQueue");
        int i=1;
        for (int v : sut) {
            _log.info(String.format("[%d] = %d", i, v));
            i++;
        }
        _log.info("remove all from priorityQueue");
        while (!sut.isEmpty()) {
            Integer v = sut.remove();
            _log.info("" + v);
        }

    }

    @Test
    public void testMedianArray() {
        MedianArray sut = new MedianArray();

        sut.add(6);
        Assert.assertNull(sut.biggestSmall());
        Assert.assertEquals((Integer) 6, sut.smallestBig());

        sut.add(8);
        Assert.assertEquals((Integer) 6, sut.biggestSmall());
        Assert.assertEquals((Integer) 8, sut.smallestBig());

        sut.add(9);
        Assert.assertEquals((Integer) 6, sut.biggestSmall());
        Assert.assertEquals((Integer) 8, sut.smallestBig());

        sut.add(10);
        Assert.assertEquals((Integer) 8, sut.biggestSmall());
        Assert.assertEquals((Integer) 9, sut.smallestBig());

    }
    public static class MedianArray {
        ArrayList<Integer> _list;
        // big numbers to on the increasing queue
        PriorityQueue<Integer> _bigs;
        // small numbers go on the decreeasing queue.
        PriorityQueue<Integer> _smalls;

        // the top of the decreasing queue should be just less than the
        // top of the increasing queue.

        public MedianArray() {
            _list = new ArrayList<>();
            // default case is that the numbers increase
            _bigs = new PriorityQueue<>();
            // reverse case is that the numbers decrease.
            _smalls = new PriorityQueue<>((x, y) -> y-x);
        }

        public Integer biggestSmall() {

            return  _smalls.peek();
        }
        public Integer smallestBig() {
            return  _bigs.peek();
        }

        public void add(int v) {
            _list.add(v);
            if (_bigs.size() == 0 && _smalls.size() == 0) {
                _bigs.add(v);
                return;
            }
            if (v < _bigs.peek()) {
                _smalls.add(v);
            } else {
                _bigs.add(v);
            }

            PriorityQueue<Integer> longer;
            PriorityQueue<Integer> shorter;

            int sizeDiff = _bigs.size() - _smalls.size();
            if (Math.abs(sizeDiff) <= 1) {
                return;
            }
            if (sizeDiff > 0) {
                longer = _bigs;
                shorter = _smalls;
            } else {
                longer = _smalls;
                shorter = _bigs;
            }
            Integer moveVal = longer.remove();
            shorter.add(moveVal);
        }
    }


}

