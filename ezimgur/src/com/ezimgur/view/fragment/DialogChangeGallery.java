package com.ezimgur.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import com.ezimgur.R;
import com.ezimgur.data.GalleryManager;
import com.ezimgur.datacontract.GallerySort;
import com.ezimgur.datacontract.SavedGallery;
import com.ezimgur.persistance.datasource.GalleryDataSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DialogChangeGallery extends EzDialogFragment {

    private OnChangeGalleryListener mChangeGalleryListener; 
	private String mCurrentGalleryName;
    private GallerySort mCurrentSort;
    private Spinner mSpinSort;
	
	public static DialogChangeGallery newInstance (String galleryName, GallerySort sort) {
		DialogChangeGallery dialog = new DialogChangeGallery();
		
		Bundle bundle = new  Bundle();
		bundle.putString("galleryName", galleryName);
        bundle.putInt("sort", sort.ordinal());
		dialog.setArguments(bundle);
		
		return dialog;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.Theme_ezimgur_Dialog);
        
        if (getArguments() != null) {
        	mCurrentGalleryName = getArguments().getString("galleryName");
            mCurrentSort = GallerySort.values()[getArguments().getInt("sort")];
        }
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mChangeGalleryListener = (OnChangeGalleryListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnChangeGalleryListener");
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	final View v = inflater.inflate(R.layout.dialog_change_gallery, container, false);
    	
    	getDialog().setTitle("change gallery");
    	getDialog().setCanceledOnTouchOutside(true);
    	
    	final Spinner spinnerGalleries = (Spinner) v.findViewById(R.id.spinnerGallery);
    	Button btnChange = (Button) v.findViewById(R.id.btnChangeGallery);
    	Button btnCancel = (Button) v.findViewById(R.id.btnCancelChangeGallery);
    	final TextView txtSubReddit = (TextView) v.findViewById(R.id.txtManualGallery);

    	//get the list of galleries, add custom one in if it was entered and not saved... then sort the list
    	List<SavedGallery> savedGalleryList = GalleryDataSource.newInstance(getActivity()).getAllGalleries();

        List<String> galleryList = new ArrayList<String>();
        for (SavedGallery gallery : savedGalleryList){
            galleryList.add(gallery.name);
        }

    	if (!galleryList.contains(mCurrentGalleryName))
    		galleryList.add(mCurrentGalleryName);
    	
    	Collections.sort(galleryList);
    	
    	final ArrayAdapter<String> galleryAdapter = 
    			new ArrayAdapter<String>(getActivity(), R.layout.sherlock_spinner_item, galleryList.toArray(new String[0]));
    	
    	galleryAdapter.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
    	spinnerGalleries.setAdapter(galleryAdapter);
    	
    	final int selected = galleryAdapter.getPosition(mCurrentGalleryName);
    	if (selected >-1)
    		spinnerGalleries.setSelection(selected);
    		
    	else {
    		txtSubReddit.setText(mCurrentGalleryName);   
    	}

        mSpinSort = (Spinner) v.findViewById(R.id.dlg_change_spin_sortby);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.sorts_array, android.R.layout.simple_spinner_item);
        mSpinSort.setAdapter(adapter);


        mSpinSort.setSelection(mCurrentSort.ordinal());


    	btnChange.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changeGalleryClicked((String)spinnerGalleries.getSelectedItem(),  txtSubReddit.getText().toString().toLowerCase());
			}
		});
    	
    	btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
    	
    	txtSubReddit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				CheckBox checkSaveSubreddit = (CheckBox) v.findViewById(R.id.checkSaveCustomGallery);
				TextView txtSaveDisclaimer = (TextView) v.findViewById(R.id.txtSaveDisclaimer);
				if (s.length()>0) {
					checkSaveSubreddit.setVisibility(View.VISIBLE);
					txtSaveDisclaimer.setVisibility(View.VISIBLE);
					spinnerGalleries.setVisibility(View.GONE);
				}
				else {
					checkSaveSubreddit.setVisibility(View.GONE);
					txtSaveDisclaimer.setVisibility(View.GONE);
					spinnerGalleries.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
		});
    	
    	return v;
    }
    
	private void changeGalleryClicked(final String targetGallery, final String target) {
		CheckBox checkSaveSubreddit = (CheckBox) getDialog().findViewById(R.id.checkSaveCustomGallery);
        CheckBox checkMakeDefault = (CheckBox) getDialog().findViewById(R.id.checkMakeDefault);
        GallerySort sort = GallerySort.TIME;
        if (((String)mSpinSort.getSelectedItem()).equalsIgnoreCase("hot"))
            sort = GallerySort.TOP;

		if (target.length() > 0 ){
			if (target.toLowerCase().contains("r/") || GalleryManager.galleryHasComments(target)) {
				mChangeGalleryListener.changeGalleryClicked(target, checkSaveSubreddit.isChecked(), sort, checkMakeDefault.isChecked());
			}
			else {
				mChangeGalleryListener.changeGalleryClicked("r/" + target, checkSaveSubreddit.isChecked(), sort, checkMakeDefault.isChecked());
			}
		}
		else {
			mChangeGalleryListener.changeGalleryClicked(targetGallery, false, sort, checkMakeDefault.isChecked());
		}
		dismiss();
	}
	
	public interface OnChangeGalleryListener {
		void changeGalleryClicked(String galleryName, boolean saveSubReddit, GallerySort sort, boolean makeDefault);
	}
}
