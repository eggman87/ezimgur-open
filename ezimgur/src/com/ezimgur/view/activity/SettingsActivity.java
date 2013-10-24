package com.ezimgur.view.activity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.ezimgur.R;
import com.ezimgur.data.SettingsManager;
import roboguice.inject.InjectView;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 11/30/12
 * Time: 9:23 PM
 */
public class SettingsActivity extends BaseActivity{

    @InjectView(R.id.sc_sett_ck_image_below) CheckBox mCheckImageBelow;
    @InjectView(R.id.sc_sett_ck_full_screen) CheckBox mCheckFullScreen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportProgressBarIndeterminateVisibility(false);

        final SettingsManager manager = new SettingsManager(this);
        mCheckImageBelow.setChecked(manager.getValue(SettingsManager.SETTING_IMAGE_BELOW, false));
        mCheckFullScreen.setChecked(manager.getValue(SettingsManager.SETTING_FULL_SCREEN_MODE, false));

        mCheckImageBelow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manager.saveValue(SettingsManager.SETTING_IMAGE_BELOW, isChecked);
            }
        });

        mCheckFullScreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manager.saveValue(SettingsManager.SETTING_FULL_SCREEN_MODE, isChecked);
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.screen_settings;
    }
}
