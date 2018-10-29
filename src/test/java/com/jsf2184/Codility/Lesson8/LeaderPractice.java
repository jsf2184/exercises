package com.jsf2184.Codility.Lesson8;

import org.junit.Assert;
import org.junit.Test;

import java.util.Stack;
import java.util.stream.IntStream;

public class LeaderPractice {

    int solve(int[] A) {
        Worker worker = new Worker(A);
        Integer res = worker.solve();
        if (res == null) {
            return -1;
        }
        return res;
    }

    @Test
    public void testSolveScenarios() {
        Assert.assertEquals(-1, solve(new int[] {}));
        Assert.assertEquals(1, solve(new int[] {1}));
        Assert.assertEquals(-1, solve(new int[] {1, 2}));
        Assert.assertEquals(-1,  solve(new int[] {1, 2, 3}));
        Assert.assertEquals(-1,  solve(new int[] {1, 2, 3, 3}));
        Assert.assertEquals(3,  solve(new int[] {1, 2, 3, 3, 3}));
        Assert.assertEquals(-1, solve(new int[] {1, 2, 3, 3, 3, 2}));
        Assert.assertEquals(3, solve(new int[] {1, 2, 3, 3, 3, 2, 3}));

    }

    @Test
    public void testCandidateScenario() {
        Assert.assertNull(Worker.findCandidate(new int[] {}));
        Assert.assertEquals(1,  Worker.findCandidate(new int[] {1}).intValue());
        Assert.assertNull(Worker.findCandidate(new int[] {1, 2}));
        Assert.assertEquals(3,  Worker.findCandidate(new int[] {1, 2, 3}).intValue());
        Assert.assertEquals(3,  Worker.findCandidate(new int[] {1, 2, 3, 3}).intValue());
        Assert.assertEquals(3,  Worker.findCandidate(new int[] {1, 2, 3, 3, 2}).intValue());
        Assert.assertNull(Worker.findCandidate(new int[] {1, 2, 3, 3, 2, 2}));
    }



    public static class Worker {
        int[] _input;

        public Worker(int[] input) {
            _input = input;
        }

        public Integer solve() {
            Integer candidate = findCandidate(_input);
            if (candidate == null) {
                return null;
            }
            long count = IntStream.of(_input).filter(i -> i == candidate).count();
            if (count > _input.length/2) {
                return candidate;
            }
            return null;
        }

        public static Integer findCandidate(int[] input) {
            Integer candidate = null;
            int count = 0;
            for (int i :  input) {
                if (candidate == null) {
                    candidate = i;
                    count = 1;
                } else if (candidate != i) {
                    count--;
                    if (count == 0) {
                        candidate = null;
                    }
                } else {
                    // it matches candidate
                    count++;
                }
            }
            return candidate;
        }
    }
}
