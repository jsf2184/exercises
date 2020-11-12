package com.jsf2184.fb.practice;

import org.junit.Test;

public class LookAndSay {
//            1
//            11
//            21
//            1211
//            111221
//            312211
//            13112221
//            1113213211
//            31131211131221
//            13211311123113112211

    @Test
    public void testLookAndSay() {
        lookAndSay(10);
    }
    public static void lookAndSay(int n) {
        String prior = "1";
        System.out.println(prior);
        for (int i=1; i<n; i++) {
            prior = generate(prior);
            System.out.println(prior);
        }
    }

    public static String generate(String prior) {
        Character lastChar = null;
        int repeat = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i<prior.length(); i++) {
            char c = prior.charAt(i);
            if (lastChar == null || c != lastChar) {
                if (lastChar != null) {
                    sb.append(repeat);
                    sb.append(lastChar);
                }
                repeat = 1;
                lastChar = c;
            } else {
                repeat++;
            }
        }
        sb.append(repeat);
        sb.append(lastChar);
        return sb.toString();
    }


}
