package com.jsf2184.se8.functional;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;

public class FunctionTests {
    // Pass in a Function.
    //    A Function takes 1 argument of a particular type and returns a potentially different type.

    int _addend = 1;

//    public <T> String callGenericFunction(T p, Function<T, String> f) {
//        String res = f.apply(p);
//        return res;
//    }

    public String callFunction(int p, Function<Integer, String> f) {
        String res =  callGenericFunction(p, f);
        return res;
    }

    public <P, T> T callGenericFunction(P param, Function<P, T> function) {
        T res = function.apply(param);
        return res;
    }

    public String toStringValWithAddend(int x) {
        return Integer.toString(x + _addend);
    }


    public String toStringVal(int x) {
        return Integer.toString(x);
    }

    @Test
    public void testFunction() {
        // Note if there is only one argument to the lambda, the parens surrounding the params are optional.
        Assert.assertEquals("5", callFunction(5, x -> {return Integer.toString(x);}));

        // Note we can omit the return if the implementation has no brackets.
        Assert.assertEquals("5", callFunction(5, (x) ->  toStringVal(x)));

        // Note that we can supply the parameter type if we want to.
        Assert.assertEquals("5", callFunction(5, (Integer x) ->  toStringVal(x)));

        Assert.assertEquals("5", callFunction(5, this::toStringVal));

    }

    @Test
    public void testFunctionAccessingMemberData() {
        Assert.assertEquals("5", callFunction(4, (x) -> {return Integer.toString(x + _addend);}));
        Assert.assertEquals("5", callFunction(4, (x) -> {return toStringVal(x + _addend);}));
        Assert.assertEquals("5", callFunction(4, this::toStringValWithAddend));
    }

    @Test
    public void testGenericFunction() {
        Assert.assertEquals("5", callGenericFunction(new MyIntHolder(5),
                                                     (mih) -> { return Integer.toString(mih.getI());
        }));
    }

    public static class MyIntHolder {
        int _i;

        public MyIntHolder(int i) {
            _i = i;
        }

        public MyIntHolder(int x, int y) {
            _i = x+ y;
        }

        public int getI() {
            return _i;
        }

        public void setI(int i) {
            _i = i;
        }
    }
    @Test
    public void testFunctionAccessingLocalData() {
        MyIntHolder myIntHolder  = new MyIntHolder(2);
        Assert.assertEquals("5", callFunction(3, (x) -> {return Integer.toString(x + myIntHolder.getI());}));
        Assert.assertEquals("5", callFunction(3, (x) -> toStringVal(x + myIntHolder.getI())));
    }

    @Test
    public void testFunctionWhereLambdaInvokesConstructor() {
        MyIntHolder res = callGenericFunction(10, (p) -> {return  new MyIntHolder(p);});
        Assert.assertEquals(10, res.getI());
        res = callGenericFunction(11, MyIntHolder::new);
        Assert.assertEquals(11, res.getI());
    }

    <P1, P2, R> R callGenericFunction(P1 p1, P2 p2, BiFunction<P1, P2, R> biFunction) {
        R res = biFunction.apply(p1, p2);
        return res;
    }

    @Test
    public void testBiFunctionWhereLambdaInvokesConstructor() {
        MyIntHolder res = callGenericFunction(3, 6, MyIntHolder::new);
        Assert.assertEquals(9, res.getI());
    }

    @Test
    public void testFunctionWhereLambdaInvokesArrayConstructor() {
        MyIntHolder[] array = callGenericFunction(10, (n) -> {
            MyIntHolder[] res = new MyIntHolder[n];
            IntStream.range(0, n).forEach(i -> {
                res[i] = new MyIntHolder(i);
            });
            return res;
        });
        Assert.assertEquals(10, array.length);
        IntStream.range(0, 10).forEach(i -> Assert.assertEquals(i, array[i].getI()));
    }


    Function<Integer, Integer> _plus2 = x -> x+2;
    Function<Integer, Integer> _times4 = x -> x * 4;



    @Test
    public void testAndThenChaining() {
        Assert.assertEquals(20,  _plus2.andThen(_times4).apply(3).intValue());
    }

    @Test
    public void testComposeChaining() {
        // compose is backwards.
        Assert.assertEquals(20,  _times4.compose(_plus2).apply(3).intValue());
    }


    @Test
    public void testFunctionAndThenChainingWithDifferentTypes() {
        Function<String, Integer> strToInt = Integer::parseInt;
        Function<Integer, Integer> square = (x) -> x * x;
        Function<Integer, String> IntToStr = (x) -> Integer.toString(x);

        Function<String, String> compound = strToInt.andThen(square).andThen(IntToStr);
        String res = callGenericFunction("4", compound);
        Assert.assertEquals("16", res);

        Assert.assertEquals("25", strToInt.andThen(square).andThen(IntToStr).apply("5"));

    }


}
