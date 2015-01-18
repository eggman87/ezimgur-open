package com.ezimgur.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.ezimgur.R;
import com.ezimgur.api.exception.ApiException;
import com.ezimgur.task.CreateMessageTask;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import roboguice.inject.InjectView;

/**
 * Created by NCR Corporation.
 * User: matthewharris
 * Date: 5/26/13
 * Time: 11:12 AM
 */
public class CreateMessageFragment extends RoboSherlockFragment {

    @InjectView(R.id.frag_create_msg_ib_send)ImageButton mBtnSend;
    @InjectView(R.id.frag_create_msg_et_to)EditText mTxtTo;
    @InjectView(R.id.frag_create_msg_et_subject)EditText mTxtSubject;
    @InjectView(R.id.frag_create_msg_et_body)EditText mTxtBody;

    private boolean mWasFirstFragment;

    public static CreateMessageFragment newInstance(){
        CreateMessageFragment messageFragment = new CreateMessageFragment();

        return messageFragment;
    }

    public static CreateMessageFragment newInstance(String composeTo){
        CreateMessageFragment messageFragment = new CreateMessageFragment();

        Bundle args = new Bundle();
        args.putString("composeTo", composeTo);
        messageFragment.setArguments(args);

        return messageFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message_create, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null){
            mTxtTo.setText(args.getString("composeTo"));
            mWasFirstFragment = true;
            mTxtSubject.requestFocus();
        }

        attachViewListeners();
    }

    public boolean wasFirstFragment(){
        return  mWasFirstFragment;
    }

    private void attachViewListeners(){
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewMessage();
            }
        });
    }

    private void createNewMessage(){
        String to = mTxtTo.getText().toString();
        String subject = mTxtSubject.getText().toString();
        String body = mTxtBody.getText().toString();

        if (!validateRequiredField(to, "to"))
            return;
        if (!validateRequiredField(body, "message"))
            return;

        new CreateMessageTask(getActivity(), to, body){

            @Override
            protected void onSuccess(Boolean aBoolean) throws Exception {
                super.onSuccess(aBoolean);
                getActivity().onBackPressed();
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                super.onException(e);
                if (e instanceof ApiException){
                    Toast.makeText(getActivity(), "unable to create: "+((ApiException)e).getErrorOnly(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "unable to create: "+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }.execute();

    }

    private boolean validateRequiredField(String field, String fieldName){
        if (field == null || field.length() == 0) {
            Toast.makeText(getActivity(), "Y U NO ENTER "+fieldName, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
