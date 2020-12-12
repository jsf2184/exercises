package com.jsf2184.dnb.parse.errors.matcher;

public class KeyWordMatcher implements IKeyWordMatcher {

    SimpleKeyWordMatcher simpleKeyWordMatcher;
    ApproximateKeyWordMatcher approximateKeyWordMatcher;

    public KeyWordMatcher(SimpleKeyWordMatcher simpleKeyWordMatcher, ApproximateKeyWordMatcher approximateKeyWordMatcher) {
        this.simpleKeyWordMatcher = simpleKeyWordMatcher;
        this.approximateKeyWordMatcher = approximateKeyWordMatcher;
    }

    @Override
    public KeyWordEnum matchWord(String word) {
        KeyWordEnum result = simpleKeyWordMatcher.matchWord(word);
        if (result == null) {
            result  = approximateKeyWordMatcher.matchWord(word);
        }
        return result;
    }
}
