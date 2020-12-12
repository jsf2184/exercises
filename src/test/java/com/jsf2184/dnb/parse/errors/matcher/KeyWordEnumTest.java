package com.jsf2184.dnb.parse.errors.matcher;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class KeyWordEnumTest {

    public static class Expectation {
        String input;
        KeyWordEnum expected;

        public Expectation(String input, KeyWordEnum expected) {
            this.input = input;
            this.expected = expected;
        }

        public String getInput() {
            return input;
        }

        public KeyWordEnum getExpected() {
            return expected;
        }
    }

    public static List<Expectation> getExpectationList() {
        return Arrays.asList(
                new Expectation("no", KeyWordEnum.NO),
                new Expectation("failed", KeyWordEnum.FAILED),
                new Expectation("incorrect", KeyWordEnum.INCORRECT),
                new Expectation("error", KeyWordEnum.ERROR),

                // some simple tests where case adjustment does the trick.
                new Expectation("NO", KeyWordEnum.NO),
                new Expectation("fAILEd", KeyWordEnum.FAILED),
                new Expectation("INCORRECT", KeyWordEnum.INCORRECT),
                new Expectation("eRRor", KeyWordEnum.ERROR),

                // special treatment for number zero
                new Expectation("0", KeyWordEnum.NUMZERO),
                new Expectation("00000", KeyWordEnum.NUMZERO),

                // tests that succeed because of special plural allowances
                new Expectation("eRRors", KeyWordEnum.ERROR),
                new Expectation("failures", KeyWordEnum.FAILURE),
                new Expectation("excepTIONS", KeyWordEnum.EXCEPTION),

                // tests that fail because there is no special plural allowance
                new Expectation("INCORRECTS", null),
                new Expectation("invalids", null),

                // And a bunch that just don't match anything
                new Expectation(null, null),
                new Expectation("", null),
                new Expectation("abc", null),
                new Expectation("00009", null),
                new Expectation("9", null)
        );
    }

    @Test
    public void testGetMaxTolerance(){
        Assert.assertEquals(2, KeyWordEnum.getMaxTolerance());
    }
    @Test
    public void testPrepForInitialSearch() {
        runPrepForInitialSearch(null, "");
        runPrepForInitialSearch("", "");
        runPrepForInitialSearch("abc", "abc");
        runPrepForInitialSearch("ABC", "abc");
        runPrepForInitialSearch("000", "0");
        runPrepForInitialSearch("009", "9");
        runPrepForInitialSearch("9", "9");
    }

    @Test
    public void testConvertToSingular() {
        runConvertToSingular(null, null);
        runConvertToSingular("", null);
        runConvertToSingular("x", null);
        runConvertToSingular("abc", null);
        runConvertToSingular("s", "");
        runConvertToSingular("abcs",  "abc");
    }

    @Test
    public void testFind() {
        // some simple tests where the case is right for the input.
        final List<Expectation> expectationList = getExpectationList();
        expectationList.forEach( exp -> runFind(exp.getInput(), exp.getExpected()) );
    }

    public void runFind(String word, KeyWordEnum expected) {
        KeyWordEnum result = KeyWordEnum.find(word);
        Assert.assertEquals(expected, result);
    }

    public void runPrepForInitialSearch(String word, String expected) {
        final String result = KeyWordEnum.prepForInitialSearch(word);
        Assert.assertEquals(expected, result);
    }

    public void runConvertToSingular(String word, String expected) {
        final String result = KeyWordEnum.convertToSingular(word);
        Assert.assertEquals(expected, result);
    }


}