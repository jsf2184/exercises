package com.jsf2184.se8.lambda;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.function.Predicate;

public class LambdaTests {
    public interface Checker {
        boolean isGood();
    }

    public static class MyChecker implements Checker {
        boolean _good;

        public MyChecker(boolean good) {
            _good = good;
        }

        @Override
        public boolean isGood() {
            return _good;
        }
    }

    public boolean callChecker(Checker checker) {
        boolean res = checker.isGood();
        return res;
    }


    @Test
    public void testMyCheckerImplementation() {
        Assert.assertTrue(callChecker(new MyChecker(true)));
        Assert.assertFalse(callChecker(new MyChecker(false)));
    }

    @Test
    public void testAnonymousImplementation() {
        Assert.assertTrue(callChecker(new Checker() {
            @Override
            public boolean isGood() {
                return true;
            }
        }));

        Assert.assertFalse(callChecker(new Checker() {
            @Override
            public boolean isGood() {
                return false;
            }
        }));

    }

    @Test
    public void testLambdaImplementation() {

        Checker trueChecker = () -> {return  true;};
        Checker falseChecker = () -> {return  false;};
        Assert.assertTrue(callChecker(trueChecker));
        Assert.assertFalse(callChecker(falseChecker));
    }

    @Test
    public void testInlineLambdaImplementation() {
        Assert.assertTrue(callChecker(() -> {return true;} ));
        Assert.assertFalse(callChecker(() -> {return false;} ));
    }


    void invokePredicate(Predicate<Integer> predicate, boolean expected) {
        invokePredicate(predicate, this.hashCode(), expected);
    }

    void invokePredicate(Predicate<Integer> predicate, int compareVal, boolean expected) {
        boolean res = predicate.test(compareVal);
        Assert.assertEquals(expected, res);
    }


    public void invokeRunnable(Runnable runnable) {
        System.out.printf("invokeRunnable(): this.hashCode = %d\n", this.hashCode());
        runnable.run();
    }

    // In next 2 tests, demonstrate that the 'this' pointer refers to a different object inside an anonymous implementation
    // while it is the same object in a lambda implementation.
    //
    @Test
    public void testPredWhoseHashcode() {
        invokePredicate(new Predicate<Integer>() {
            @Override
            public boolean test(Integer val) {
                return val == this.hashCode();
            }
        }, false);

        invokePredicate(hc -> hc == this.hashCode(), true);
    }

    @Test
    public void testPredWhoseHashcodeWithVariables() {
        Predicate<Integer> predicateAnonymous = new Predicate<Integer>() {
            @Override
            public boolean test(Integer val) {
                return val == this.hashCode();
            }
        };
        invokePredicate(predicateAnonymous, false);

        Predicate<Integer> predicateLambda = hc -> hc == this.hashCode();
        invokePredicate(predicateLambda, true);
    }

    @Test
    public void testPredThatGetsResultFromEffectivelyFinalLocalVariable() {
        int compareVal = 10;
        invokePredicate((Integer x) -> x == compareVal, 10, true);
        invokePredicate((x) -> x == compareVal, 11, false);
    }

    // Add some member variables that are used in the following test.
    int _memberVariable = 10;
    CountDownLatch _cdl = new CountDownLatch(1);

    @Test
    public void testLamdbaRunnableThatChangesMemberVariable() throws InterruptedException {
        Thread thread = new Thread(() -> {
            _memberVariable++;
            _cdl.countDown();
        });
        thread.start();
        _cdl.await();
        Assert.assertEquals(11, _memberVariable);
    }

}
