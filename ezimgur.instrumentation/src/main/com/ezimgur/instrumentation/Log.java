package com.ezimgur.instrumentation;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 11/17/12
 * Time: 7:15 PM
 */
public class Log {
    public static boolean LOG_ENABLED = false;
    public static Type type = Type.ANDROID;
    private static ILogger logger;

    public enum Type {
        ANDROID,
        LOG4J
    }

    protected static  ILogger getLogger() {
        if (logger != null)
            return logger;

        if (type == Type.ANDROID)
            logger = new AndroidLogger();
        else if (type == Type.LOG4J)
            logger = new JavaLogger();
        return logger;
    }

    public static void i(String tag, String string) {
        if (LOG_ENABLED) getLogger().info(tag, string);
    }
    public static void e(String tag, String string) {
        if (LOG_ENABLED) getLogger().exception(tag, string);
    }
    public static void e(String tag, String message, Throwable tr) {
        if (LOG_ENABLED) getLogger().exception(tag, message, tr);
    }
    public static void d(String tag, String string) {
        if (LOG_ENABLED) getLogger().debug(tag, string);
    }
    public static void d(String tag, String message, Throwable tr) {
        if (LOG_ENABLED) getLogger().debug(tag, message, tr);
    }
    public static void v(String tag, String string) {
        if (LOG_ENABLED) getLogger().verbose(tag, string);
    }
    public static void w(String tag, String string) {
        if (LOG_ENABLED) getLogger().warning(tag, string);
    }
}
