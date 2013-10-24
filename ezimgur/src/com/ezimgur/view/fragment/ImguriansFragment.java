package com.ezimgur.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ezimgur.R;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;

/**
 * Created by NCR Corporation.
 * User: matthewharris
 * Date: 5/25/13
 * Time: 11:31 AM
 */
public class ImguriansFragment extends RoboSherlockFragment {

    public static ImguriansFragment newInstance(){
        ImguriansFragment fragment = new ImguriansFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_imgurians, null);
    }
}
