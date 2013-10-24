package com.ezimgur.instrumentation;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/16/12
 * Time: 1:38 PM
 */
public interface ILogger {

    public void info(String tag, String msg);
    public void exception(String tag, String string);
    public void exception(String tag, String message, Throwable tr);
    public void debug(String tag, String string);
    public void debug(String tag, String message, Throwable tr);
    public void verbose(String tag, String string);
    public void warning(String tag, String string);
}
