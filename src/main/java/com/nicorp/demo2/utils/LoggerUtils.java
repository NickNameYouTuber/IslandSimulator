package com.nicorp.demo2.utils;

import java.util.logging.Logger;

public class LoggerUtils {
    private static final Logger logger = Logger.getLogger(LoggerUtils.class.getName());

    public static void log(String message) {
        logger.info(message);
    }
}