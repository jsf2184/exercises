package com.jsf2184.hackerrank;

import org.junit.Assert;
import org.junit.Test;

public class CountingValleys {

    @Test
    public void testValleys() {
        String hike = "UDDDUDUU";
        Assert.assertEquals(1, countValleys(hike.length(), hike));
    }
    public static int countValleys(int n, String s) {

        int steps = Math.min(n, s.length());
        int prev = 0;
        int level = 0;
        int valleys = 0;
        int mountains = 0;

        for (int i = 0; i < steps; i++) {
            char step = s.charAt(i);
            if (step == 'U') {
                level += 1;
            } else if (step == 'D') {
                level -= 1;
            } else {
                // bad input
                continue;
            }
            if (level == 0) {
                if (prev < 0) {
                    // finished climbing back from a valley
                    valleys++;
                } else if (prev > 0) {
                    mountains++;
                }
            }
            prev = level;
        }
        return  valleys;

    }

}
