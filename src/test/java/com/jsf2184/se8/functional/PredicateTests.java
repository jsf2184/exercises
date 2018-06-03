package com.jsf2184.se8.functional;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.Predicate;

public class PredicateTests {
    // Pass in a Predicate.
    //    A Predicate takes 1 arguments of a particular type and returns a boolean.


    public static boolean isEven(int n) {
        return  n % 2 == 0;
    }
    public boolean callPredicate(Predicate<Integer> predicate, int i) {
        boolean res = predicate.test(i);
        return res;
    }

    @Test
    public void testPredicateCalling() {
        Assert.assertTrue(callPredicate((i) -> {return i% 2 == 0;}, 4));
        Assert.assertFalse(callPredicate((i) -> {return i% 2 == 1;}, 4));
    }

    @Test
    public void testPredicateImplementedByStaticMethod() {
        Assert.assertTrue( callPredicate(i -> isEven(i), 2));
        Assert.assertTrue( callPredicate( PredicateTests::isEven, 2));
        Predicate<Integer> predicate = PredicateTests::isEven;
        Assert.assertFalse( callPredicate( predicate, 3));
    }

    @Test
    public void testPredicateChaining() {
        Predicate<Integer> pred2 = x -> x%2 == 0;
        Predicate<Integer> pred3 = x -> x%3 == 0;
        
        // and testing
        Assert.assertFalse(callPredicate(pred2.and(pred3), 2));
        Assert.assertFalse(callPredicate(pred2.and(pred3), 3));
        Assert.assertTrue(callPredicate(pred2.and(pred3), 6));

        // or testing
        Assert.assertFalse(callPredicate(pred2.or(pred3), 1));
        Assert.assertTrue(callPredicate(pred2.or(pred3), 2));
        Assert.assertTrue(callPredicate(pred2.or(pred3), 3));
        Assert.assertTrue(callPredicate(pred2.or(pred3), 6));

        Assert.assertTrue(callPredicate(pred2.or(pred3).negate(), 1));
        Assert.assertFalse(callPredicate(pred2.or(pred3).negate(), 2));
        Assert.assertFalse(callPredicate(pred2.or(pred3).negate(), 3));
        Assert.assertFalse(callPredicate(pred2.or(pred3).negate(), 6));

    }


}
