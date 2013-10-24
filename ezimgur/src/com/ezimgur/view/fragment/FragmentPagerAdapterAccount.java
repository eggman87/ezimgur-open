package com.ezimgur.view.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.ezimgur.instrumentation.Log;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/28/12
 * Time: 7:37 PM
 */
public class FragmentPagerAdapterAccount extends FragmentStatePagerAdapter {

    public String[] titles = new String[]{"notifications", "likes", "submissions", "settings", "stats", };
    private static final String TAG = "EzImgur.FragmentPagerAdapter";

    public FragmentPagerAdapterAccount(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "getting account fragment @ "+position);

        switch (position){
            case 0:
                return NotificationsFragment.newInstance();
            case 1:
                return LikesFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
