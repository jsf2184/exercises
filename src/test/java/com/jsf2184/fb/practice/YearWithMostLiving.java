package com.jsf2184.fb.practice;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.IntStream;

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
                                             new Lifespan(2003, 2003));
        Assert.assertEquals(2003, getYearWithMostLiving(lives));
        Assert.assertEquals(2003, getYearWithMostLiving2(lives));

    }

    public static int getYearWithMostLiving(List<Lifespan> lives) {
        int earliestYear = 1900;
        int lastYear = 2020;
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
        TreeMap<Integer, Integer> birthYears = new TreeMap<>();
        TreeMap<Integer, Integer> deathYears = new TreeMap<>();

        for(Lifespan lifespan :lives) {
            Integer yearCount = birthYears.computeIfAbsent(lifespan.birthYear, k -> 0);
            yearCount++;
            birthYears.put(lifespan.birthYear, yearCount);
            yearCount = deathYears.computeIfAbsent(lifespan.deathYear+1, k -> 0);
            yearCount++;
            deathYears.put(lifespan.deathYear+1, yearCount);
        }

        int runningPopulation = 0;
        int highestPopulation = 0;
        int highestYear = 0;
        while(!deathYears.isEmpty()) {
            Integer nextBirthYear = birthYears.isEmpty() ? null : birthYears.firstKey();
            int nextDeathYear = deathYears.firstKey();

            int nextYear = nextBirthYear == null ? nextDeathYear : Math.min(nextBirthYear, nextDeathYear);

            if (nextBirthYear != null && nextBirthYear == nextYear) {
                runningPopulation += birthYears.remove(nextBirthYear);
            }
            if (nextDeathYear == nextYear) {
                runningPopulation -= deathYears.remove(nextDeathYear);
            }
            if (runningPopulation > highestPopulation) {
                highestPopulation = runningPopulation;
                highestYear = nextYear;
            }
        }
        return highestYear;
    }

}
