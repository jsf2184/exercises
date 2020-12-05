package com.jsf2184.esight;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DayMath {
    enum Day {
        Monday(0),
        Tuesday(1),
        Wednesday(2),
        Thursday(3),
        Friday(4),
        Saturday(5),
        Sunday(6);

        private final int num;

        Day(int num) {
            this.num = num;
        }

        private static final Day[] numToDay = new Day[Day.values().length];
        private static final Map<String, Day> nameMap = new HashMap<>();
        static {
            Arrays.stream(Day.values()).forEach(d -> {
                numToDay[d.num] = d;
                nameMap.put(d.name().toUpperCase(), d);
            });
        }

        public static Day toDay(String s) {
            return s == null ? null : nameMap.get(s.toUpperCase());
        }

        public Day addDay(int numDays) {
            final int resultNum = (num + numDays) % 7;
            final Day day = numToDay[resultNum];
            return day;
        }
    }

    @Test
    public void testAdd() {
        testOne(null, 1, null);
        testOne("bad", 1, null);
        testOne("friday", 3, Day.Monday);
        testOne("friday", 10, Day.Monday);
        testOne("friday", 31, Day.Monday);
        testOne("monDay", 2, Day.Wednesday);
        testOne("monDay", 9, Day.Wednesday);
        testOne("monDay", 16, Day.Wednesday);


    }

    public void testOne(String d, int numDays, Day expected) {
        final Day day = Day.toDay(d);
        if (day == null) {
            Assert.assertNull(expected);
            return;
        }
        final Day result = day.addDay(numDays);
        Assert.assertEquals(expected, result);
    }

}
