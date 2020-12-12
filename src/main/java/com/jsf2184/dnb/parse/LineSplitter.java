package com.jsf2184.dnb.parse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LineSplitter {

    public List<String> delimSplit(String message) {
        String[] strings = message.split("\\P{Alpha}+");
        ArrayList<String> result = new ArrayList<>(Arrays.asList(strings));
        return result;
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
        StringBuilder sb = new StringBuilder();
        int consecUppers = 0;
        for (int i=0; i<word.length(); i++) {
            char c = word.charAt(i);
            if (Character.isUpperCase(c)) {
                // We got an uppercase letter. It marks the start of a new word if we transitioned from
                // a lower case letter.
                //
                if (consecUppers == 0 && sb.length() > 0) {
                    words.add(sb.toString());
                    sb = new StringBuilder();
                }
                consecUppers++;
            } else {
                // We got a lower case letter. We need to figure out if its just a new letter on an existing word
                // or is it a transition to a new word? Examples...
                //   - EATmore   (the 'm' marks a new word) - criterion:  before the 'm', consecUppers > 1
                //   - eatMore   (the 'o' does not mark a new word) - criterion: before the 'o', consecUppers == 1
                //   - eatMore   (the 'r' does not mark a new work) - criterion: before the 'r', consecUppers == 0
                if (consecUppers > 1) {
                    // Something like 'EATmore" - the transition starts a new word
                    words.add(sb.toString());
                    sb = new StringBuilder();
                }
                consecUppers = 0;
            }
            sb.append(c);
        }
        if (sb.length() > 0) {
            words.add(sb.toString());
        }

    }
}
