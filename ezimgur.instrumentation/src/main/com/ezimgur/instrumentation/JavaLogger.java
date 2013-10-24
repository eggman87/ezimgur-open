package com.ezimgur.instrumentation;

import org.apache.commons.logging.impl.Log4JLogger;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/16/12
 * Time: 1:38 PM
 */
public class JavaLogger implements ILogger {

    private static Log4JLogger logger;

    protected static Log4JLogger getLogger() {
        if (logger == null)
            logger = new Log4JLogger("ezimgur");
        return logger;
    }

    @Override
    public void info(String tag, String msg) {
        getLogger().info(tag + " | " + msg);
    }

    @Override
    public void exception(String tag, String string) {
        getLogger().error(tag + " | " + string);
    }

    @Override
    public void exception(String tag, String message, Throwable tr) {
        getLogger().error(tag + " | " + message, tr);
    }

    @Override
    public void debug(String tag, String string) {
        getLogger().debug(tag + " | " + string);
    }

    @Override
    public void debug(String tag, String message, Throwable tr) {
        getLogger().debug(tag + " | " + message, tr);
    }

    @Override
    public void verbose(String tag, String string) {
        //getLogger().
    }

    @Override
    public void warning(String tag, String string) {

    }
}
