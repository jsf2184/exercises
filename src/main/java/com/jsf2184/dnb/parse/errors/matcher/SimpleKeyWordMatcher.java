package com.jsf2184.dnb.parse.errors.matcher;

public class SimpleKeyWordMatcher implements IKeyWordMatcher {

    @Override
    public KeyWordEnum matchWord(String word) {
        // Hopefully we have a simple match based on a simple enum string search
        return KeyWordEnum.find(word);
    }
}
