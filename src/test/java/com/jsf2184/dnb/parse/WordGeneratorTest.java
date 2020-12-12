package com.jsf2184.dnb.parse;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WordGeneratorTest {

    @Test
    public void testDelimSplit() {
        WordGenerator wordGenerator = new WordGenerator();
        List<String> strings = wordGenerator.delimSplit("abc4$defGhi   \t,,,#$xyz   ");
        Assert.assertEquals(Arrays.asList("abc", "defGhi", "xyz"), strings);

        strings = wordGenerator.delimSplit("abc3  def,# $ % g \t \n h");
        Assert.assertEquals(Arrays.asList("abc", "def", "g", "h"), strings);

    }

    public void testCamelCaseSplit() {
    }

    @Test
    public void testCamelCaseSplitOfWord() {
        List<String> words = new ArrayList<>();
        WordGenerator.splitWordOnCaseChanges("THEcowJumpedOVERtheMoonI", words);
        Assert.assertEquals(Arrays.asList("THE", "cow", "Jumped", "OVER", "the", "Moon", "I"), words);
    }
}