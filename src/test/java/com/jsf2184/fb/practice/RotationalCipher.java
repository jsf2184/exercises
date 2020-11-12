package com.jsf2184.fb.practice;

import org.junit.Assert;
import org.junit.Test;

public class RotationalCipher {
    public static class Range {
        char low;
        char high;
        int count;
        int rotationFactor;

        public Range(char low, char high, int rotationFactor) {
            this.low = low;
            this.high = high;
            count = (high - low) + 1;
            this.rotationFactor = rotationFactor % count;
        }

        boolean inRange(char c) {
            return c >= low && c <= high;
        }

        char getRotatedValue(char c) {
            int result = c + rotationFactor;
            if (result  > high) {
                int offset = (result - high)  - 1;
                result = low + offset;
            }
            return (char) result;
        }
    }

    public String rotationalCipher(String input, int rotationFactor) {
        // Write your code here
        Range lowerCase = new Range('a', 'z', rotationFactor);
        Range upperCase = new Range('A', 'Z', rotationFactor);
        Range digits = new Range('0', '9', rotationFactor);

        StringBuilder sb = new StringBuilder();

        for (int i=0; i<input.length(); i++) {
            char c = input.charAt(i);
            Range range = null;
            if (lowerCase.inRange(c)) {
                range = lowerCase;
            } else if (upperCase.inRange(c)) {
                range = upperCase;
            } else if (digits.inRange(c)) {
                range = digits;
            }

            if (range == null) {
                sb.append(c);
            } else {
                sb.append(range.getRotatedValue(c));
            }
        }
        return sb.toString();
    }


    @Test
    public void testRange() {
        Range range = new Range('0', '9', 1);
        Assert.assertEquals('0', range.getRotatedValue('9'));
        Assert.assertEquals('9', range.getRotatedValue('8'));
        range = new Range('0', '9', 11);
        Assert.assertEquals('0', range.getRotatedValue('9'));
        Assert.assertEquals('9', range.getRotatedValue('8'));
    }

    @Test
    public void testIt() {
        String input1 =  "All-convoYs-9-be:Alert1.";
        String expected1 = "Epp-gsrzsCw-3-fi:Epivx5.";
        Assert.assertEquals(expected1, rotationalCipher(input1, 4));
        String input2 = "abcdZXYzxy-999.@";
        String expected2 = "stuvRPQrpq-999.@";
        Assert.assertEquals(expected2, rotationalCipher(input2, 200));
    }
}
