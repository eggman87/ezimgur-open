package com.ezimgur.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ezimgur.R;
import com.ezimgur.app.EzApplication;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/8/12
 * Time: 2:21 PM
 */
public class DialogUserProfile extends EzDialogFragment {


    public static DialogUserProfile newInstance() {
        DialogUserProfile dialog = new DialogUserProfile();

        return dialog;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NORMAL, R.style.Theme_ezimgur_Dialog);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.screen_account, container, false);

//        TextView txtBio = (TextView) v.findViewById(R.id.tv_profile_bio);
//        txtBio.setText("loading profile...");

        getDialog().setTitle(EzApplication.getAccountUserName());
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.Gainsboro));

        return v;
    }


}
