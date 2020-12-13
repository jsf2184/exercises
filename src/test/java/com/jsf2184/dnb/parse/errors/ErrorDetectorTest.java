package com.jsf2184.dnb.parse.errors;

import com.jsf2184.dnb.parse.errors.matcher.IKeyWordMatcher;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ErrorDetectorTest {

    ErrorDetector errorDetector;
    WordGenerator wordGenerator;
    IKeyWordMatcher keyWordMatcher;

    @Test
    public void hasErrorTests() {
        errorDetector = new ErrorDetector();
        runTest("got arrors", true);
        runTest("errors found ", true);
        runTest("0 errors found ", false);
        runTest("020 errors found ", true);
        runTest("got exceptins", true);
        runTest("caught nullExceptins", true);
        runTest("caught no nullExceptins ", false);
        runTest("caught 00 nullExceptins", false);
        runTest("caught no nullExceptins but caught another ioException", true);
        runTest("caught no nullExceptins but found 3 other errs", true);
    }

    private void runTest( String message, boolean expected) {
        boolean result = errorDetector.hasErrors(message);
        Assert.assertEquals(expected, result);
    }
}