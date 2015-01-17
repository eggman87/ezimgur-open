package com.ezimgur.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.ezimgur.data.SettingsManager;
import com.ezimgur.view.utils.ViewUtils;

import javax.swing.text.View;

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

        Intent intent = new Intent(this, GalleryCompactActivity.class);
        startActivity(intent);
    }
}
