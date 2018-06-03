package com.jsf2184.se8.streams;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StreamSourceTests {

    public int[] populateArrayWithRangeOfNumbers(int n) {
        int values[] = new int[n];
        IntStream.range(0, n).forEach(i-> values[i] = i);
        return values;
    }

    @Test
    public void testNonBoxedIntegerStreamingToForEach() {
        int values[] = new int[100];
        Set<Integer> set = IntStream.range(0, 100).boxed().collect(Collectors.toSet());
        Assert.assertEquals(100, set.size());
        IntStream.range(0, 100).forEach(i -> Assert.assertTrue(set.contains(i)));
        IntStream.range(100, 200).forEach(i -> Assert.assertFalse(set.contains(i)));
    }

    @Test
    public void testNonBoxedIntStreamingToLongSum() {
        long sum1 = IntStream.range(0, 100).asLongStream().sum();
        Assert.assertEquals(sum1, 99 * 100/2);
    }

    @Test
    public void testIntArrayStreamingToLongSum() {
        int[] array = populateArrayWithRangeOfNumbers(100);
        // this is pretty fast because we are not boxing the longs.
        long sum1 = Arrays.stream(array).asLongStream().sum();
        Assert.assertEquals(sum1, 99 * 100/2);
    }


}
