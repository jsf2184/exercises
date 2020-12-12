package com.jsf2184.dnb.parse;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LineSplitterTest  {

    @Test
    public void testDelimSplit() {
        LineSplitter lineSplitter = new LineSplitter();
        List<String> strings = lineSplitter.delimSplit("abc4$defGhi   \t,,,#$xyz   ");
        Assert.assertEquals(Arrays.asList("abc", "defGhi", "xyz"), strings);
    }

    public void testCamelCaseSplit() {
    }

    @Test
    public void testCamelCaseSplitOfWord() {
        List<String> words = new ArrayList<>();
        LineSplitter.camelCaseSplit("THEcowJumpedOVERtheMoonI", words);
        Assert.assertEquals(Arrays.asList("THE", "cow", "Jumped", "OVER", "the", "Moon", "I"), words);
    }
}