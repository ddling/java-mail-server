package com.ddling.utils;

import org.apache.log4j.*;

import java.io.IOException;

/**
 * Created by ddling on 2014/12/24.
 */
public class LoggerFactory {

    private static String logPath = "log/server.log";

    public static Logger getLogger(Class clazz) {
        Logger logger = Logger.getLogger(clazz);

        FileAppender fileAppender = null;
        ConsoleAppender consoleAppender = null;

        try {
            fileAppender = new FileAppender(new TTCCLayout(), logPath);
            consoleAppender = new ConsoleAppender(new TTCCLayout());
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.addAppender(fileAppender);
        logger.addAppender(consoleAppender);

        return logger;
    }
}
