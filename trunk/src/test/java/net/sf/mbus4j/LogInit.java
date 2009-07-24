/*
 * Created on 10.02. 2007
 * 
 * $Id:Rs232Test.java 192 2007-02-09 15:26:20Z aploese $
 *
 */
package net.sf.mbus4j;

import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author aploese
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
    public static synchronized void initLog(String level){
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
