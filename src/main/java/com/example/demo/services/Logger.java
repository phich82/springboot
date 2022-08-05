package com.example.demo.services;

import org.slf4j.LoggerFactory;


public class Logger {

    /**
     * Log with SLF4J
     *
     * @param _thisTarget
     * @return org.apache.logging.log4j.Logger
     */
    private static org.slf4j.Logger _logger(Object _thisTarget) {
        return LoggerFactory.getLogger(_thisTarget.getClass());
    }

    // /**
    //  * Log4j2 without SLF4J
    //  *
    //  * @param _thisTarget
    //  * @return org.apache.logging.log4j.Logger
    //  */
    // private static org.apache.logging.log4j.Logger _logger(Object _thisTarget) {
    //     return org.apache.logging.log4j.LogManager.getLogger(_thisTarget.getClass());
    // }

    public static void info(String message, Object _thisTarget) {
        _logger(_thisTarget).info(message);
    }

    public static void warn(String message, Object _thisTarget) {
        _logger(_thisTarget).warn(message);
    }

    public static void debug(String message, Object _thisTarget) {
        _logger(_thisTarget).debug(message);
    }

    public static void error(String message, Object _thisTarget) {
        _logger(_thisTarget).error(message);
    }
}
