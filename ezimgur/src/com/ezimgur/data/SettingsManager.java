package com.ezimgur.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 1/19/13
 * Time: 10:08 PM
 */
public class SettingsManager {

    public static final String SETTING_IMAGE_BELOW = "image_sits_below";
    public static final String SETTING_FULL_SCREEN_MODE = "fullscreen_mode";
    public static final String SETTING_USE_OLD_LAYOUT = "use_old_layout";
    public static final String SETTING_USE_WEB_GIF_VIEWER = "use_web_gifs";

    private SharedPreferences mPreferences;

    public SettingsManager(Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveValue(String key, String value){
        mPreferences.edit().putString(key, value).commit();
    }

    public void saveValue(String key, boolean value) {
        mPreferences.edit().putBoolean(key, value).commit();
    }

    public String getString(String key, String defaultValue) {
        return mPreferences.getString(key, defaultValue);
    }

    public boolean getValue(String key, boolean defaultValue) {
        return mPreferences.getBoolean(key, defaultValue);
    }
}
