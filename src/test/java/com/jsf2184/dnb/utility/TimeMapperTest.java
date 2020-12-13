package com.jsf2184.dnb.utility;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;

import static org.junit.Assert.*;

public class TimeMapperTest {

    @Test
    public void roundTripTest() {
        String str = "2020-04-01 10:10:09";
        Long seconds = TimeMapper.toDateSeconds(str);
        String roundTrip = TimeMapper.toDateString(seconds);
        Assert.assertEquals(str, roundTrip);
    }

    @Test
    public void testBadParseReturnsNull()  {
        String str = "2020-04-01 10:10:0x";
        Long seconds = TimeMapper.toDateSeconds(str);
        Assert.assertNull(seconds);
    }

}