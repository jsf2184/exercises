package com.jsf2184.utility;

import org.apache.log4j.*;

public class LoggerUtility
{
    static public void initRootLogger() {
        Logger logger = Logger.getRootLogger();
        PatternLayout layout = new PatternLayout("%d %-5p %m%n");
        ConsoleAppender appender = new ConsoleAppender(layout);
        logger.addAppender(appender);
        logger.setLevel(Level.INFO);
    }
}
