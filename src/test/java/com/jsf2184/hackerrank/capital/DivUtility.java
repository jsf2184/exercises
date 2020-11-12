package com.jsf2184.hackerrank.capital;

import java.util.Random;

public class DivUtility {
    public static char getRandomChar(Random random) {
        final String choices = "abcd";
        int numChoices = choices.length();
        int idx = random.nextInt(numChoices);
        return choices.charAt(idx);
    }

    public static String getRandomString(Random random, int len) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<len; i++) {
            sb.append(getRandomChar(random));
        }
        return sb.toString();
    }

}
