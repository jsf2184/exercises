package com.jsf2184;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
public class WordDistance {
    LevenshteinDistance distance = new LevenshteinDistance(2);

    @Test
    public void distance() {
        runIt("eror", "error");
        runIt("erpor", "error");
        runIt("rror", "error");
        runIt("exeption", "exception");
        runIt("abc", "def");
        runIt("abcd", "abdc");
    }

    @Test
    public void splitIt() {
        splitEm("a b c d  ");
    }

    public List<String> splitEm(String s) {
        String[] arr = s.split(" ", 1);
        List<String> strings = Arrays.asList(arr);
        log.info("{}", strings);
        return strings;
    }

    public void runIt(String s1, String s2) {
        log.info("{} {} {}", s1, s2, distance.apply(s1, s2));
    }
}
