package com.jsf2184.bb;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class WordPaths {

    @Test
    public void superSimple() {
        Set<String> dictionary = new HashSet<>(Arrays.asList("abc", "def"));
        verifyProcess(dictionary, "defabc", Collections.singletonList("def abc"));
    }

    @Test
    public void multiSolutions() {
        Set<String> dictionary = new HashSet<>(Arrays.asList("abc", "def", "d", "efa", "bc"));
        verifyProcess(dictionary, "defabc", Arrays.asList("def abc", "d efa bc"));
    }

    @Test
    public void aFalseLead() {
        Set<String> dictionary = new HashSet<>(Arrays.asList("abch", "def", "d", "efa", "bc"));
        verifyProcess(dictionary, "defabch", Collections.singletonList("def abch"));
    }

    @Test
    public void noSolutions() {
        Set<String> dictionary = new HashSet<>(Arrays.asList("abc", "def", "d", "efa", "bc"));
        verifyProcess(dictionary, "defabcg", Collections.emptyList());
    }

    public static void verifyProcess(Set<String> dictionary, String input, List<String> expected) {
        Set<String> expectedSet = new HashSet<>(expected);
        final Set<String> results = process(dictionary, input);
        Assert.assertEquals(expectedSet, results);
    }

    /**
     * @param dictionary  - A set of the words in our dictionary, In interview, this may have been specified as a Map
     *                      but a set is adequate
     * @param input - The input string in which we are looking for words
     * @return - A Set of results of space delimted words
     */
    public static Set<String> process(Set<String> dictionary, String input) {
        Queue<Path> queue = new LinkedList<>();
        queue.add(new Path());
        List<Path> winners = new ArrayList<>();
        while (!queue.isEmpty()) {
            Path path = queue.remove();
            if (path.length == input.length()) {
                // we have a winner!
                winners.add(path);
            }
            // See if we can form any child paths from this path.
            int startPosition = path.length;
            int remainingLength = input.length() - startPosition;
            for (String candidate : dictionary) {
                // Is this word a candidate to add to our path?
                final int candidateLen = candidate.length();
                if (candidateLen <= remainingLength && candidate.equals(input.substring(startPosition, startPosition+candidateLen))) {
                    Path newPath = new Path(path, candidate);
                    queue.add(newPath);
                }
            }
        }

        Set<String> results = new HashSet<>();
        for (Path winner : winners) {
            StringBuilder sb = new StringBuilder();
            for (String word : winner.words) {
                if (sb.length() > 0) {
                    sb.append(" ");
                }
                sb.append(word);
            }
            results.add(sb.toString());
        }
        return results;
    }


    public static class Path {
        int length;
        List<String> words;

        public Path() {
            length = 0;
            words = new ArrayList<>();
        }
        public Path(Path priorPath, String newWord) {
            length = priorPath.length + newWord.length();
            // we need our own copy of the list;
            words = new ArrayList<>(priorPath.words);
            words.add(newWord);
        }
    }


}
