package com.jsf2184.hackerrank;

import org.junit.Assert;
import org.junit.Test;

public class CloudJumping {


    static int jumpingOnClouds(int[] c) {
        int last = c.length - 1;
        int current = 0;
        int numSteps = 0;
        while (current < last) {
            int step2 = current + 2;
            if (step2 <= last && c[step2] == 0) {
                current += 2;
                numSteps++;
                continue;
            }
            current++;
            numSteps++;
        }
        return  numSteps;
    }

    @Test
    public void test1() {
        int[] c = {0,0,1,0,0,1,0};
        Assert.assertEquals(4, jumpingOnClouds(c));
        int [] d = {0,1,0,0,0,1,0};
        Assert.assertEquals(3, jumpingOnClouds(d));

    }
}
