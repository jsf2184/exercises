package com.jsf2184.dnb.parse.errors;

import com.jsf2184.dnb.LogEntry;
import com.jsf2184.dnb.utility.TimeMapper;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class LogParserTest {

    private static final String TIMESTAMP = "2020-04-01 10:10:04";
    private static final Long TIMESTAMP_SECONDS = TimeMapper.toDateSeconds(TIMESTAMP);
    LogParser logParser = new LogParser();

    @Test
    public void testParse() {
        validateParse("2020-04-01 10:10:04 ServiceClient - Service processed row",
                      new LogEntry(TIMESTAMP_SECONDS, "ServiceClient", "Service processed row")) ;

        validateParse("2020-04-01 10:10:04 ServiceClient - S",
                      new LogEntry(TIMESTAMP_SECONDS, "ServiceClient", "S")) ;

        validateParse("2020-04-01 10:10:04 ServiceClient - ",
                      new LogEntry(TIMESTAMP_SECONDS, "ServiceClient", "")) ;

        validateParse("2020-04-01 10:10:0x ServiceClient - Service processed row", null) ;

        validateParse("2020-04-01 10:10:04 ServiceClient -", null) ;
        validateParse("2020-04-01 10:10:04 ServiceClient", null) ;
        validateParse("2020-04-01 10:10:04 S", null) ;
        validateParse("2020-04-01 10:10:04_S", null) ;
        validateParse("2020-04-01 10:10:04", null) ;
        validateParse("2020-04-01 10:10:0x", null) ;
        validateParse("", null) ;
        validateParse(null, null) ;

    }
    private  void validateParse(String line, LogEntry expected) {
        LogEntry logEntry = logParser.parseLogEntry(line, 1);
        Assert.assertEquals(expected, logEntry);

    }
}