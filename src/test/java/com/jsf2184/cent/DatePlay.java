package com.jsf2184.cent;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class DatePlay {
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    Date convert(String dateStr) throws ParseException {
        final Date result = dateFormat.parse(dateStr);
        return result;
    }

    @Test
    public void testGoodConvert() throws ParseException {
        final Date date1 = convert("12/28/2020");
        final Date date2 = convert("01/28/2021");
        final Date date3 = convert("12/28/2021");

        TreeMap<Date, Integer> map = new TreeMap<>();
        map.put(date1, 1);
        map.put(date2, 2);
        map.put(date3, 3);
        Assert.assertEquals(new Integer(1), search("12/20/2020", map));
        Assert.assertEquals(new Integer(1), search("12/28/2020", map));

        Assert.assertEquals(new Integer(2), search("12/29/2020", map));
        Assert.assertEquals(new Integer(2), search("01/28/2021", map));

        Assert.assertEquals(new Integer(3), search("02/28/2021", map));
        Assert.assertEquals(new Integer(3), search("12/28/2021", map));

        Assert.assertNull(search("12/28/2022", map));


    }

    public Integer search(String dateStr, TreeMap<Date, Integer> map) throws ParseException {
        final Date dateKey = convert(dateStr);
        final Integer result = map.get(dateKey);
        if (result != null) {
            return result;
        }
        final Map.Entry<Date, Integer> higherEntry = map.higherEntry(dateKey);
        if (higherEntry == null) {
            return null;
        }
        return higherEntry.getValue();
    }

}
