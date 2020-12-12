package com.jsf2184.dnb.parse.errors.matcher;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.junit.Assert;
import org.junit.Test;

public class ApproximateKeyWordMatcherTest {

    ApproximateKeyWordMatcher distanceMatcher = new ApproximateKeyWordMatcher();

    @Test
    public void matchWord() {
        // tolerance for the 'a' in error
        verifyMatchWord("ArroR", KeyWordEnum.ERROR);
        // and the plural exemption even thought it would only have tolerance for one correction.
        verifyMatchWord("arroRs", KeyWordEnum.ERROR);

        // FAILD has tolerance for 1 correction, but not for plurals
        verifyMatchWord("FAILD", KeyWordEnum.FAILED);
        verifyMatchWord("FAILDS", null); // but no plurals

        // EXCEPTION has tolerance for 2 corrections and plurals
        verifyMatchWord("Exceptin", KeyWordEnum.EXCEPTION);
        verifyMatchWord("eXceptn", KeyWordEnum.EXCEPTION);
        verifyMatchWord("eXceptns", KeyWordEnum.EXCEPTION);
        verifyMatchWord("except", null);  // but not 3
    }

    private void verifyMatchWord(String word, KeyWordEnum expected ) {
        final KeyWordEnum result = distanceMatcher.matchWord(word);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void getCalculator() {

        // Make sure this test matches current state of KeywordEnum
        Assert.assertEquals(2, KeyWordEnum.getMaxTolerance());

        verifyGetCalculator(0, false);
        verifyGetCalculator(1, true);
        verifyGetCalculator(2, true);
        verifyGetCalculator(3, false);
    }

    @Test
    public void selectWordForMatching() {
        // here we focus on whether or not DistanceMatcher is using the proper word considering whether or
        // not singularWord is null and whether or not the enum in question allowsPlurals. THe actual
        // values (beyond their nullness or not) are meaningless

        // should use "singularWord" because KeywordEnum.ERROR accepts plurals
        verifySelectMatchingWord("prepWord", "singularWord", KeyWordEnum.ERROR, "singularWord");
        // should avoid "singularWord" because KeywordEnum.INVALID does not accept plurals
        verifySelectMatchingWord("prepWord", "singularWord", KeyWordEnum.INVALID, "prepWord");
        // should avoid "singularWord" because there is none
        verifySelectMatchingWord("prepWord", null, KeyWordEnum.ERROR, "prepWord");
        // should avoid "singularWord" because there it is empty
        verifySelectMatchingWord("prepWord", "", KeyWordEnum.ERROR, "prepWord");
    }

    private void verifySelectMatchingWord(String prepWord, String singularWord, KeyWordEnum wordEnum, String expected) {
        final String result = ApproximateKeyWordMatcher.selectWordForMatching(prepWord, singularWord, wordEnum);
        Assert.assertEquals(expected, result);
    }

    private void verifyGetCalculator(int tolerance, boolean expectSuccess) {
        final LevenshteinDistance calculator = distanceMatcher.getCalculator(tolerance);
        if (!expectSuccess) {
            Assert.assertNull(calculator);
            return;
        }
        Assert.assertNotNull(calculator);
        Assert.assertEquals(tolerance, calculator.getThreshold().intValue());
    }
}