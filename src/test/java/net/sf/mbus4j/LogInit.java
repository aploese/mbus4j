/*
 * mbus4j - Open source drivers for mbus protocol see <http://www.m-bus.com/ > - http://mbus4j.sourceforge.net/
 * Copyright (C) 2009  Arne Plöse
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/ >.
 */
package net.sf.mbus4j;

import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class LogInit {

    /**
     *
     */
    public static final String TRACE = "trace";
    /**
     *
     */
    public static final String DEBUG = "debug";
    /**
     *
     */
    public static final String INFO = "info";
    /**
     *
     */
    public static final String WARN = "warn";
    /**
     *
     */
    public static final String ERROR = "error";
    /**
     *
     */
    public static final String FATAL = "fatal";

    /**
     *
     * @param level
     */
    public static synchronized void initLog(String level) {
        Properties props = new Properties();
        props.setProperty("log4j.appender.stdout",
                "org.apache.log4j.ConsoleAppender");
        props.setProperty("log4j.appender.stdout.Target", "System.out");
        //log4j.appender.stdout=org.apache.log4j.FileAppender
        //log4j.appender.stdout.File=Easy.log
        props.setProperty("log4j.appender.stdout.layout",
                "org.apache.log4j.PatternLayout");
        props.setProperty("log4j.appender.stdout.layout.ConversionPattern",
                "%d{ABSOLUTE} %5p %c{1}: %m%n");

        //set log levels - for more verbose logging change 'info' to 'debug' ###

        props.setProperty("log4j.rootLogger", level + ", stdout");

        PropertyConfigurator.configure(props);
    }
}
