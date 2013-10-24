package com.ezimgur.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/29/12
 * Time: 12:45 PM
 */
public class SubmissionsFragment extends RoboSherlockFragment {

    public static SubmissionsFragment newInstance() {
        SubmissionsFragment fragment = new SubmissionsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
