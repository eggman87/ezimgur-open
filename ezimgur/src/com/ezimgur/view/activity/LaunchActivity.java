package com.ezimgur.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.ezimgur.data.SettingsManager;

/**
 * Copyright NCR Inc,
 * User: matthewharris
 * Date: 12/25/13
 * Time: 9:27 PM
 */
public class LaunchActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SettingsManager manager = new SettingsManager(this);

        boolean useOldLayout =  manager.getValue(SettingsManager.SETTING_USE_OLD_LAYOUT, false);
        if (useOldLayout) {
            Intent intent = new Intent(this, GalleryActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, GalleryCompactActivity.class);
            startActivity(intent);
        }
    }
}
