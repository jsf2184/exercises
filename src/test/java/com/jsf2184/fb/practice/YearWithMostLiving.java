package com.jsf2184.fb.practice;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.IntStream;

@Slf4j
public class YearWithMostLiving {
    public static class Lifespan {
        int birthYear;
        int deathYear;

        public Lifespan(int birthYear, int deathYear) {
            this.birthYear = birthYear;
            this.deathYear = deathYear;
        }
    }

    @Test
    public void testIt() {
        List<Lifespan> lives = Arrays.asList(new Lifespan(2000, 2003),
                                             new Lifespan(2002, 2005),
                                             new Lifespan(1999, 2005),
                                             new Lifespan(2006, 2025),
                                             new Lifespan(2008, 2015),
                                             new Lifespan(2009, 2015),
                                             new Lifespan(2012, 2019),
                                             new Lifespan(2011, 2013));
        int result1 = getYearWithMostLiving(lives);
        int result2 = getYearWithMostLiving2(lives);
        log.info("Result1 = {}, Result2 = {}", result1, result2);
        Assert.assertEquals(2003, result1, result2);

    }

    public static int getYearWithMostLiving(List<Lifespan> lives) {
        int earliestYear = 1900;
        int lastYear = 2040;
        int[] counts = new int[(lastYear-earliestYear)];
        for (Lifespan lifespan : lives) {
            for (int year = lifespan.birthYear; year <= lifespan.deathYear; year++ ) {
                counts[year-earliestYear]++;
            }
        }
        int bestYearValue = -1;
        int bestIdx = -1;

        for (int i=0; i<counts.length; i++) {
            if (counts[i] > bestYearValue) {
                bestYearValue = counts[i];
                bestIdx = i;
            }
        }
        return earliestYear + bestIdx;
    }


    public static int getYearWithMostLiving2(List<Lifespan> lives) {
        TreeMap<Integer, Integer> eventYears = new TreeMap<>();

        for(Lifespan lifespan :lives) {
            Integer yearCount = eventYears.computeIfAbsent(lifespan.birthYear, k -> 0);
            yearCount++;
            eventYears.put(lifespan.birthYear, yearCount);
            yearCount = eventYears.computeIfAbsent(lifespan.deathYear+1, k -> 0);
            yearCount--;
            eventYears.put(lifespan.deathYear+1, yearCount);
        }

        int runningPopulation = 0;
        int highestPopulation = 0;
        int highestYear = 0;
        while(!eventYears.isEmpty()) {
            Map.Entry<Integer, Integer> event = eventYears.pollFirstEntry();
            Integer year = event.getKey();
            Integer delta = event.getValue();
            runningPopulation += delta;
            if (runningPopulation > highestPopulation) {
                highestPopulation = runningPopulation;
                highestYear = year;
            }
        }
        return highestYear;
    }

}
