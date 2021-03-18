package com.jsf2184.ucourse.docsign;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class MostDivisible {


    // Not exactly the question I received but here is a reasonable sub-problem
    // Consider the array of keys [2, 3, 4, 4, 8, 24]
    // For each key, how many of the non-one keys are divisible in each of them
    // Answers:
    //  2(1) : 2
    //  3(1) : 3
    //  4(3) : 2, 4, 4
    //  4(3) : 2, 4, 4
    //  8(4) : 2, 4, 4, 8
    // 24(6) : 2, 3, 4, 4, 8, 24

    public static int maxDivisibilty(int[] keys) {
        Arrays.sort(keys);
        int maxDivisors = 0;
        for (int k=0; k<keys.length; k++) {
            int key = keys[k];
            int numDivisors = 0;
            for (int i=0; i<=k; i++) {
                if (key % keys[i] == 0) {
                    numDivisors++;
                }
            }
            log.info("for key: {}, numDivisors is {}", key, numDivisors);
            maxDivisors = Math.max(maxDivisors, numDivisors);
        }
        return maxDivisors;
    }


    @Test
    public void test() {
        Assert.assertEquals(3, maxDivisibilty(new int[] {2, 4, 4}));
        Assert.assertEquals(6, maxDivisibilty(new int[] {2, 3, 4, 4, 8, 24}));
    }
}
