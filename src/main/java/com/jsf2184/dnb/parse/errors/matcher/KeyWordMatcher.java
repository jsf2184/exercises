package com.jsf2184.dnb.parse.errors.matcher;

public class KeyWordMatcher implements IKeyWordMatcher {

    IKeyWordMatcher simpleKeyWordMatcher;
    IKeyWordMatcher approximateKeyWordMatcher;

    // Constructor for dependency injection
    public KeyWordMatcher(IKeyWordMatcher simpleKeyWordMatcher, IKeyWordMatcher approximateKeyWordMatcher) {
        this.simpleKeyWordMatcher = simpleKeyWordMatcher;
        this.approximateKeyWordMatcher = approximateKeyWordMatcher;
    }

    // Constructor for convenience


    public KeyWordMatcher() {
        this(new SimpleKeyWordMatcher(), new ApproximateKeyWordMatcher());
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
