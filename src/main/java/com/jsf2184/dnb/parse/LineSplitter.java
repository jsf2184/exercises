package com.jsf2184.dnb.parse;

import java.util.List;

public class LineSplitter {

    public List<String> delimSplit(String message) {

    }

    public void camelCaseSplit(List<String> words) {
        final int size = words.size();
        for (int i=0; i<size; i++) {
            camelCaseSplit(words.get(i), words);
        }
    }

    /**
     * @param word - A word to try to split due to upper-lower case changes. The original
     *               word is already on the 'words' list, so it does not need to be placed there.
     * @param words - If 'word' can be split into 2 or more sub-words, place each sub-word on the
     *                words list
     */

    enum CharType {
        NotAlpha,
        Upper,
        Lower,
    }
    public static void camelCaseSplit(String word, List<String> words) {
        Boolean prevUpper = null;
        for (int i=0; i<word.length(); i++) {
            char c = word.charAt(i);
            if (Character.isUpperCase(c)) {

            }
        }
    }
}
