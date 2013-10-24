package com.ezimgur.view.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ezimgur.R;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 11/18/12
 * Time: 2:04 PM
 */
public class DialogLoading extends EzDialogFragment {

    private String mTitle;
    private String mMessage;
    private boolean mNeedToShowOnPostResume;

    public static DialogLoading newInstance (String title, String message) {
        DialogLoading dialogFragment = new DialogLoading();

        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        dialogFragment.setArguments(bundle);

        return dialogFragment;
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        try {
            return super.show(transaction, tag);
        } catch (Exception e) {
            mNeedToShowOnPostResume = true;
        }
        return -1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        setStyle(STYLE_NORMAL, R.style.Theme_ezimgur_Dialog);


        if (getArguments() != null) {
            mTitle = getArguments().getString("title");
            mMessage = getArguments().getString("message");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.dialog_loading, container, false);

        getDialog().setTitle(mTitle);
        getDialog().setCanceledOnTouchOutside(false);

        TextView messageTextView = (TextView) v.findViewById(R.id.dialog_load_message_tv);
        messageTextView.setText(mMessage);

        return v;
    }


}
