package com.ezimgur.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.ezimgur.R;
import com.ezimgur.app.EzApplication;
import com.ezimgur.datacontract.Message;
import com.ezimgur.task.CreateMessageTask;
import com.ezimgur.task.DeleteMessageTask;
import com.ezimgur.task.GetMessageThreadTask;
import com.ezimgur.view.adapter.MessageThreadAdapter;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import roboguice.inject.InjectView;

import java.util.List;

/**
 * Created by NCR Corporation.
 * User: matthewharris
 * Date: 5/25/13
 * Time: 12:20 PM
 */
public class MessageDetailFragment extends RoboSherlockFragment {

    @InjectView(R.id.frag_msg_detail_tv_subject)TextView mTxtSubject;
    @InjectView(R.id.frag_msg_detail_progress) ProgressBar mProgressIndicator;
    @InjectView(R.id.frag_msg_detail_lv_thread) ListView mListThread;
    @InjectView(R.id.frag_msg_detail_et_reply)EditText mTextReply;
    @InjectView(R.id.frag_msg_details_btn_send)Button mBtnSend;
    @InjectView(R.id.frag_msg_detail_ib_delete)ImageButton mBtnDelete;

    private List<Message> mMessages;
    private int mParentId = 0;

    public static MessageDetailFragment newInstance(int messageId) {
        MessageDetailFragment detailFragment = new MessageDetailFragment();

        Bundle args = new Bundle();
        args.putInt("messageId", messageId);

        detailFragment.setArguments(args);
        //detailFragment.setRetainInstance(true);

        return detailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message_detail, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Bundle args = getArguments();
        if (args != null) {
            mParentId = args.getInt("messageId");
        }

        loadMessageThread(mParentId);

        attachViewListeners();
    }

    private void loadMessageThread(int parentId){
        mProgressIndicator.setVisibility(View.VISIBLE);
        new GetMessageThreadTask(getActivity(), parentId){

            @Override
            protected void onSuccess(List<Message> messages) throws Exception {
                super.onSuccess(messages);
                mProgressIndicator.setVisibility(View.GONE);
                setMessageThread(messages);
            }


            @Override
            protected void onException(Exception e) throws RuntimeException {
                super.onException(e);
                mProgressIndicator.setVisibility(View.GONE);
            }
        }.execute();
    }

    public void setMessageThread(List<Message> messages){
        mMessages = messages;
        mTxtSubject.setText(messages.get(0).subject);
        mListThread.setAdapter(new MessageThreadAdapter(messages));
    }

    private void attachViewListeners(){
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replyOnThread();
            }
        });

        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteThread();
            }
        });
    }

    private void replyOnThread(){
        String reply = mTextReply.getText().toString();

        if (reply.length() > 0) {

            String myUsername = EzApplication.getAccountUserName();
            String toUsername = "";
            for (Message message : mMessages){
                if (!message.from.equals(myUsername)){
                    toUsername = message.from;
                    break;
                }
            }
            if (toUsername.equals(""))
                toUsername = myUsername;

            final int parentId = mMessages.get(0).parentId;
            new CreateMessageTask(getActivity(), toUsername, null, reply, parentId){

                @Override
                protected void onSuccess(Boolean aBoolean) throws Exception {
                    super.onSuccess(aBoolean);
                    loadMessageThread(parentId);
                    mTextReply.setText("");
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mTextReply.getWindowToken(), 0);
                }
            }.execute();
        }
    }

    private void deleteThread(){
        new DeleteMessageTask(getActivity(), mParentId){

            @Override
            protected void onSuccess(Boolean aBoolean) throws Exception {
                super.onSuccess(aBoolean);

                getActivity().onBackPressed();
            }
        }.execute();
    }
}
