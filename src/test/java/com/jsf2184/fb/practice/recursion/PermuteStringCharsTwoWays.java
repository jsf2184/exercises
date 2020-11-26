package com.jsf2184.fb.practice.recursion;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class PermuteStringCharsTwoWays {

    @Test
    public void testIt() {
        System.out.println("ResultsWithoutPfx");
        ArrayList<String> resultsWithoutPfx = new ArrayList<>();
        permuteWithoutPrefix("abcd", resultsWithoutPfx);
        resultsWithoutPfx.forEach(System.out::println);

        System.out.println("");

        System.out.println("ResultsWithPfx");
        ArrayList<String> resultsWithPfx = new ArrayList<>();
        permuteWithPrefix("", "abcd", resultsWithPfx);
        resultsWithPfx.forEach(System.out::println);
        Assert.assertEquals(resultsWithoutPfx, resultsWithPfx);

        System.out.println("PermuteWithoutRecursionAndStack");
        ArrayList<String> resultsWithoutRecursion = new ArrayList<>();
        permuteWithoutRecursionAndStack("abcd", resultsWithoutRecursion);
        resultsWithoutRecursion.forEach(System.out::println);
        Assert.assertEquals(new HashSet<>(resultsWithoutPfx), new HashSet<>(resultsWithoutRecursion));

        System.out.println("PermuteWithoutRecursionAndQueue");
        resultsWithoutRecursion.clear();
        permuteWithoutRecursionAndQueue("abcd", resultsWithoutRecursion);
        resultsWithoutRecursion.forEach(System.out::println);
        Assert.assertEquals(new HashSet<>(resultsWithoutPfx), new HashSet<>(resultsWithoutRecursion));


    }
    public void permuteWithoutPrefix(String s, List<String> results) {
        if (s.length() == 1) {
            results.add(s);
        }
        for (int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            String remainder = s.substring(0, i) + s.substring(i+1);
            int priorSize = results.size();
            permuteWithoutPrefix(remainder, results);
            for (int p=priorSize; p<results.size(); p++) {
                results.set(p, c + results.get(p));
            }
        }
    }

    public void permuteWithPrefix(String pfx, String s, List<String> results) {
        if (s.length() == 1) {
            results.add(pfx + s);
        }
        for (int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            String remainder = s.substring(0, i) + s.substring(i+1);
            permuteWithPrefix(pfx+c, remainder, results);
        }
    }

    public static class Job {
        String pfx;
        String remainder;

        public Job(String pfx, String remainder) {
            this.pfx = pfx;
            this.remainder = remainder;
        }

    }


    public void permuteWithoutRecursionAndStack(String s, List<String > results) {
        Stack<Job> stack = new Stack<>();
        stack.push(new Job("", s));
        while (!stack.isEmpty()) {
            Job job = stack.pop();
            if (job.remainder.length() == 1) {
                results.add(job.pfx + job.remainder);
            } else {
                for (int i=0; i <job.remainder.length(); i++) {
                    char c = job.remainder.charAt(i);
                    String others = job.remainder.substring(0, i) + job.remainder.substring(i+1);
                    Job childJob = new Job(job.pfx+c, others);
                    stack.push(childJob);
                }
            }
        }
    }

    public void permuteWithoutRecursionAndQueue(String s, List<String > results) {
        Queue<Job> queue = new LinkedList<>();
        queue.add(new Job("", s));
        while (!queue.isEmpty()) {
            Job job = queue.remove();
            if (job.remainder.length() == 1) {
                results.add(job.pfx + job.remainder);
            } else {
                for (int i=0; i <job.remainder.length(); i++) {
                    char c = job.remainder.charAt(i);
                    String others = job.remainder.substring(0, i) + job.remainder.substring(i+1);
                    Job childJob = new Job(job.pfx+c, others);
                    queue.add(childJob);
                }
            }
        }
    }


}
