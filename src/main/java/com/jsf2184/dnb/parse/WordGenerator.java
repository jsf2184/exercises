package com.jsf2184.dnb.parse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WordGenerator {


    /**
     * delimSplit() - Do a preliminary delimter based split, splitting on any non-alphabetic character
     *                Note that non alphabetic characters are disposed of during the split.
     *
     * example "abc3  def,# $ % g \t \n h" -> ["abc", "def" "g", "h"]
     * @param message - message to split
     * @return - resultan array of words
     */
    public List<String> delimSplit(String message) {
        String[] strings = message.split("\\P{Alpha}+");
        ArrayList<String> result = new ArrayList<>(Arrays.asList(strings));
        return result;
    }


    /**
     * splitWordListOnCaseChanges - for each word on words list, attempt to split that
     * word into additional words, keying on changes between upper and lower case letters.
     *
     * example: ["applePie", "goodAndSweet"] -> ["applePie", "goodAndSweet", "apple", "Pie", "good", "And", "Sweet"]
     * @param words
     */
    public void splitWordListOnCaseChanges(List<String> words) {
        final int size = words.size();
        for (int i=0; i<size; i++) {
            splitWordOnCaseChanges(words.get(i), words);
        }
    }

    /**
     *
     * supplementWordsWithCaseChangeSplits() - supplement our list of words with additional
     * words based on case-change splitting. For example the word: "inputException" results
     * in us adding "input" and "Exception" to the words list.
     *
     * @param word - A word to try to split due to upper-lower case changes. The original
     *               word is already on the 'words' list, so it does not need to be placed there.
     * @param words - If 'word' can be split into 2 or more sub-words, place each sub-word on the
     *                words list
     */
    public static void splitWordOnCaseChanges(String word, List<String> words) {
        StringBuilder wordBuf = new StringBuilder();
        int consecUppers = 0;
        for (int i=0; i<word.length(); i++) {
            char c = word.charAt(i);
            if (Character.isUpperCase(c)) {
                // We got an uppercase letter. It marks the start of a new word if we transitioned from
                // a lower case letter.
                //
                if (consecUppers == 0 && wordBuf.length() > 0) {
                    words.add(wordBuf.toString());
                    wordBuf = new StringBuilder();
                }
                consecUppers++;
            } else {
                // We got a lower case letter. We need to figure out if its just a new letter on an existing word
                // or is it a transition to a new word? Examples...
                //   - EATmore   (the 'm' marks a new word) - criterion:  before the 'm', consecUppers > 1
                //   - eatMore   (the 'o' does not mark a new word) - criterion: before the 'o', consecUppers == 1
                //   - eatMore   (the 'r' does not mark a new work) - criterion: before the 'r', consecUppers == 0
                if (consecUppers > 1) {
                    // Something like 'EATmore" - the transition starts a new word, and we add 'EAT' to our list
                    words.add(wordBuf.toString());
                    wordBuf = new StringBuilder();
                }
                consecUppers = 0;
            }
            // whether we started a new word, or we are just adding on to an existing word, add our latest char.
            wordBuf.append(c);
        }
        if (wordBuf.length() > 0) {
            words.add(wordBuf.toString());
        }

    }
}
