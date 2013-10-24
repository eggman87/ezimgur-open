package com.ezimgur.instrumentation;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/16/12
 * Time: 1:42 PM
 */
public class AndroidLogger implements ILogger {

    @Override
    public void info(String tag, String msg) {
        android.util.Log.i(tag, msg);
    }

    @Override
    public void exception(String tag, String string) {
        android.util.Log.e(tag, string);
    }

    @Override
    public void exception(String tag, String message, Throwable tr) {
        android.util.Log.e(tag, message, tr);
    }

    @Override
    public void debug(String tag, String string) {
        android.util.Log.d(tag, string);
    }

    @Override
    public void debug(String tag, String message, Throwable tr) {
        android.util.Log.d(tag, message, tr);
    }

    @Override
    public void verbose(String tag, String string) {
        android.util.Log.v(tag, string);
    }

    @Override
    public void warning(String tag, String string) {
        android.util.Log.w(tag, string);
    }
}
