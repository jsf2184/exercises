package com.jsf2184.mlm;

import org.apache.commons.lang3.mutable.MutableInt;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ArrayPractice {
    @Test
    public void hello() {
        System.out.println("Hello world");
    }
    @Test
    public void intArrays() {
        int[] array1 = null;
        Assert.assertNull(array1);
        int array2[] = new int[3];
        Assert.assertEquals(3, array2.length);
        Assert.assertEquals(0, array2[0]);
        for (int v : array2) {
            Assert.assertEquals(0, v);
        }
        for(int i=0; i< array2.length; i++) {
            Assert.assertEquals(0, array2[i]);
        }

        int sum1 = Arrays.stream(array2).boxed().map((i) -> i+1).mapToInt(i-> i).sum();
        Assert.assertEquals(3, sum1);

        int[] array3 = new int[] {0, 1, 2, 3};
        int[] array4 = {0, 1, 2, 3};
        Assert.assertArrayEquals(array3, array4);

        Assert.assertEquals(6, Arrays.stream(array3).sum());

        List<Integer> list5 = IntStream.range(0, 3).boxed().collect(Collectors.toList());
        Assert.assertEquals(3, list5.size());
        Optional<Integer> sum = list5.stream().reduce((prev, val) -> prev + val);
        Assert.assertEquals(3, (int) sum.get());

    }

    public static class Array {
        public static  int[] of(int... elements) {
            return elements;
        }
    }

    @Test
    public void testArrayOf() {
        Assert.assertArrayEquals(new int[] {1, 2, 3}, Array.of(1, 2, 3) );
    }

    @Test
    public void testLoop() {
        int sum1 = 0;
        int [] arr = Array.of(1, 2, 3);
        for (int i : arr)  {
             sum1 += i;
        }
        List<Integer> list = Arrays.asList(1,2,3);
        final MutableInt sum2 = new MutableInt(0);
        list.forEach(sum2::add);
        Assert.assertEquals(sum1, sum2.intValue());
    }

    @Test
    public void stringArrays() {
        String[] array1 = new String[4];
        Arrays.stream(array1).forEach(s -> Assert.assertNull(s));
        Arrays.stream(array1).forEach(Assert::assertNull);
        String[] array2 = new String[] {"a", "b", "c"};
        String[] array3 =  {"a", "b", "c"};
    }

    @Test
    public void twoDim() {

        int[][] arr = new int[4][2];
        Assert.assertEquals(4, arr.length);
        Assert.assertEquals(2, arr[3].length);
        arr[3][1] = 10;
        Assert.assertEquals(10, arr[3][1]);

    }

    public static class C {
        int x;
        int y;

        public C(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            C c = (C) o;
            return x == c.x && y == c.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

}
