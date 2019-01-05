package com.jsf2184.se8.streams;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class PrimitiveStreamTests {

    @Test
    public void testIntStreamIterate() {
        IntStream.iterate(0, i -> i + 3).limit(100 / 3 + 1).forEach(System.out::println);
    }

    @Test
    public void testExplicitValues() {
        IntStream.of(2,4, 5,9).forEach(System.out::println);

    }

    @Test
    public void buildMap() {

        Map<Integer, Integer> map = IntStream.range(0, 10).boxed().collect(Collectors.toMap(i -> i, i -> i * i));

        map.forEach((key, value) -> System.out.printf("%d -> %d\n", key, value));

        map.forEach((k, v) -> System.out.printf("%d -> %d\n", k, v));
    }

    public int[] createArray(int size) {
        int res[] = new int[size];
        IntStream.range(0, size).forEach(i -> res[i] = i);
        return res;
    }

    @Test
    public void buildAndIterateThroughArray() {
        int[] array = createArray(10);
        IntStream.of(array).forEach(System.out::println);

        // If we want to get the total as an int
        int total = IntStream.of(array).sum();
        System.out.printf("Sum is %d\n", total);

        // If we want to get the total as a long
        long longTotal = IntStream.of(array).asLongStream().sum();
        System.out.printf("LongSum is %d\n", longTotal);

        // If we want to get the total as a long but involving boxing
        long sum = Arrays.stream(array).asLongStream().sum();
        System.out.printf("LongSum is %d\n", sum);

        // Finally a really bad way to do it using reduce
        Optional<Long> reducedSum = Arrays.stream(array).asLongStream().boxed().reduce(Long::sum);
        reducedSum.ifPresent(s -> System.out.printf("reduced Sum is %d\n", s));
    }

    @SuppressWarnings("Convert2MethodRef")
    @Test
    public void sumArrayStepByStepAsPrimitves() {
        int[] array = createArray(10);
        // Note that things stay as primitives throughout
        IntStream intStream1 = Arrays.stream(array);
        LongStream longStream1 = intStream1.asLongStream();
        longStream1.forEach((long l) -> System.out.println(l) );

        IntStream intStream2 = IntStream.of(array);
        LongStream longStream2 = intStream2.asLongStream();
        longStream2.forEach((long l) -> System.out.println(l) );

        // Compiler error if try to take a LongStream directly of an int[] so the following has to be commented out
//      LongStream longStream3 = LongStream.of(array)
    }
}
