package com.jsf2184.date;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateTests {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/y") ;
    LocalDate independenceDay = LocalDate.of(1776, 7, 4);
    LocalDate christmas2019 = LocalDate.of(2019, 12, 25);

    @Test
    public void testDateParse() {
        testDate("7/4/1776");
        testDate("07/04/1776");
    }

    @Test
    public void testDatePrint()
    {
        Assert.assertEquals("7/4/1776", toString(independenceDay));
        Assert.assertEquals("12/25/2019", toString(christmas2019));
    }
    public LocalDate convert(final String src)
    {
        LocalDate res = LocalDate.parse(src, formatter);
        return res;
    }

    public String toString(LocalDate src)
    {
        String res = src.format(formatter);
        return res;
    }
    public void testDate(String src)
    {
        LocalDate actual = convert(src);
        LocalDate expected = LocalDate.of(1776, 7, 4) ;
        Assert.assertEquals(expected, actual);

    }
}
