package com.jsf2184.se8.functional;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BiFunctionTests {
    // Pass in a BiFunction.
    //    A BiFunction takes 2 arguments of possibly different types and returns a 3rd type.
    public String callBiFunction(int p1,
                                 int p2,
                                 BiFunction<Integer, Integer, String> bif)
    {
        String res = bif.apply(p1, p2);
        return res;
    }

    public String toStringAdd(int x, int y) {
        return Integer.toString(x+y);
    }

    @Test
    public void testBiFunction() {
        Assert.assertEquals("8", callBiFunction(3, 5, (x, y) -> {return Integer.toString(x+y);}));
        Assert.assertEquals("8", callBiFunction(3, 5, (x, y) -> {return toStringAdd(x, y);}));
        Assert.assertEquals("8", callBiFunction(3, 5, this::toStringAdd));

    }

    @Test
    public void testAndThenChaining() {
        List<Integer> numbers = IntStream.range(0, 100).boxed().collect(Collectors.toList());
        BiFunction<List<Integer>, Integer, List<Integer>> divisibleBy = (l, f) ->
                l.stream().filter(i -> i % f == 0).collect(Collectors.toList());

        Function<List<Integer>, List<Integer>> ge90 = (l) ->
                l.stream().filter(i -> i >= 90).collect(Collectors.toList());

        List<Integer> res = divisibleBy.andThen(ge90).apply(numbers, 3);
        List<Integer> expected = Arrays.asList(90, 93, 96, 99);
        Assert.assertEquals(res, expected);



    }
}
