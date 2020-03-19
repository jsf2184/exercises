package com.jsf2184.utility;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class UtilityTests {

    private static final Logger _log = Logger.getLogger(UtilityTests.class);


    @BeforeClass
    public static void setup()
    {
        LoggerUtility.initRootLogger();
    }

    @Test
    public void testCreateArray() {
        int[] array = Utility.createArray(10);
        _log.info(Arrays.toString(array));
    }

    @Test
    public void testShuffle() {
        int[] res = Utility.shuffle(10);
        _log.info(Arrays.toString(res));
    }


    @Test
    public void testReverseWithEvenNumberElements() {
        int[] array = Utility.createArray(10);
        Utility.reverse(array);
        _log.info(Arrays.toString(array));
    }

    @Test
    public void testReverseWithOddNumberElements() {
        int[] array = Utility.createArray(9);
        Utility.reverse(array);
        _log.info(Arrays.toString(array));
    }

    @Test
    public void testReverseWithZeroElements() {
        int[] array = Utility.createArray(0);
        Utility.reverse(array);
        _log.info(Arrays.toString(array));
    }

    @Test
    public void testReverseWithOneElements() {
        int[] array = Utility.createArray(1);
        Utility.reverse(array);
        _log.info(Arrays.toString(array));
    }

    @Test
    public void testSubstringEnd() {
        String str = "abc";
        Assert.assertEquals("abc", str.substring(0));
        Assert.assertEquals("bc", str.substring(1));
        Assert.assertEquals("c", str.substring(2));
        Assert.assertEquals("", str.substring(3));


    }

    @Test
    public void testBadSplices() {
        verifyBadSplice(null, 0);
        verifyBadSplice("", 0);
        verifyBadSplice("a", 1);
        verifyBadSplice("abc", 3);
    }

    @Test
    public void testGoodSplices() {
        verifyGoodSplice("abc", 0, "bc");
        verifyGoodSplice("abc", 1, "ac");
        verifyGoodSplice("abc", 2, "ab");
    }

    @Test
    public void testPermute() {
        testPermute("abcd");
        testPermute("abc");
        testPermute("ab");
        testPermute("a");
    }

    @Test
    public void testFibs() {
        for (int n=0; n<10; n++) {
            _log.info(String.format("fib(%d) = %d = %d",
                                    n,
                                    Utility.fib(n),
                                    Utility.recurseFib(n)));
        }
    }

    public void testPermute(String s) {
        Utility.resetPermuteCount();
        ArrayList<String> res = new ArrayList<>();
        Utility.permute("", s, res);
        _log.info(String.format("splices=%d, permutes=%d: %s",
                Utility.getPermuteCount(), res.size(), res));

    }

    public void verifyBadSplice(String s, int x) {
        boolean caught = false;
        try {
            Utility.splice(s, x);
        } catch (IllegalArgumentException ignore) {
            caught = true;
        }
        Assert.assertTrue(caught);
    }

    public void verifyGoodSplice(String s, int x, String expected) {
        String res = Utility.splice(s, x);
        Assert.assertEquals(expected, res);
    }

}
