package com.ezimgur.view.activity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.ezimgur.R;
import com.ezimgur.data.SettingsManager;
import com.ezimgur.view.utils.ViewUtils;
import roboguice.inject.InjectView;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 11/30/12
 * Time: 9:23 PM
 */
public class SettingsActivity extends BaseActivity{

    @InjectView(R.id.sc_sett_ck_full_screen) CheckBox mCheckFullScreen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportProgressBarIndeterminateVisibility(false);

        boolean defaultUseOldLayout = ViewUtils.isTabletInLandscapeMode(this);

        final SettingsManager manager = new SettingsManager(this);
        mCheckFullScreen.setChecked(manager.getValue(SettingsManager.SETTING_FULL_SCREEN_MODE, false));

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
