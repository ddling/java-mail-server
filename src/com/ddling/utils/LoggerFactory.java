/*
 * Copyright (C) 2014 lingdongdong
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
