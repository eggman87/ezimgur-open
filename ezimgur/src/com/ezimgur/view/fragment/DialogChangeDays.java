package com.ezimgur.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.ezimgur.R;
import com.ezimgur.datacontract.GallerySort;
import com.ezimgur.view.event.ChangeDaysAgoEvent;
import com.google.inject.Inject;
import roboguice.event.EventManager;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 11/18/12
 * Time: 8:54 PM
 */
public class DialogChangeDays extends EzDialogFragment {


    @Inject protected EventManager mEventManager;
    private EditText mTxtDaysAgo;
    private int mCurrentPage;
    private GallerySort mCurrentSort;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NORMAL, R.style.Theme_ezimgur_Dialog);
        if (getArguments() != null) {
            mCurrentPage = getArguments().getInt("currentPage");
            mCurrentSort = GallerySort.values()[getArguments().getInt("currentSort")];
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.dialog_change_daysago, container, false);

        getDialog().setTitle("refine");

        mTxtDaysAgo = (EditText) v.findViewById(R.id.dlg_days_et_daysago);
        mTxtDaysAgo.setText(String.valueOf(mCurrentPage));

        Button btnDaysAgo = (Button) v.findViewById(R.id.dialog_daysago_btnok);

        btnDaysAgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int daysAgo = 0;
                try {
                    daysAgo= Integer.parseInt(mTxtDaysAgo.getText().toString());
                } catch (Exception e){

                }

                mEventManager.fire(new ChangeDaysAgoEvent(daysAgo));
                dismiss();
            }
        });

        return v;
    }

    public static DialogChangeDays newInstance (int currentPage, GallerySort currentSort) {
        DialogChangeDays dialogFragment = new DialogChangeDays();

        Bundle bundle = new Bundle();
        bundle.putInt("currentPage", currentPage);
        bundle.putInt("currentSort", currentSort.ordinal());
        dialogFragment.setArguments(bundle);

        return dialogFragment;
    }
}
