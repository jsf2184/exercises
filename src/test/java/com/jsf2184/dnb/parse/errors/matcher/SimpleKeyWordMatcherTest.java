package com.jsf2184.dnb.parse.errors.matcher;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class SimpleKeyWordMatcherTest {
    SimpleKeyWordMatcher simpleKeyWordMatcher = new SimpleKeyWordMatcher();

    @Test
    public void matchWord() {
        final List<KeyWordExpectation> expectationList = KeyWordExpectation.getExpectationList();
        expectationList.forEach( exp -> runMatchWord(exp.getInput(), exp.getExpected()) );
    }

    void runMatchWord(String word, KeyWordEnum expected) {
        final KeyWordEnum wordEnum = simpleKeyWordMatcher.matchWord(word);
        Assert.assertEquals(expected, wordEnum);
    }
}