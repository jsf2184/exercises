package com.jsf2184.dnb.parse.errors.matcher;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class KeyWordEnumTest {

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
        final List<KeyWordExpectation> expectationList = KeyWordExpectation.getExpectationList();
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