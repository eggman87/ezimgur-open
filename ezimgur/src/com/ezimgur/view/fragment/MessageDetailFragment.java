package com.ezimgur.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.ezimgur.R;
import com.ezimgur.api.impl.conversation.request.GetConversationRequest;
import com.ezimgur.app.EzApplication;
import com.ezimgur.datacontract.Conversation;
import com.ezimgur.datacontract.Message;
import com.ezimgur.task.CreateMessageTask;
import com.ezimgur.task.DeleteMessageTask;
import com.ezimgur.task.GetMessageThreadTask;
import com.ezimgur.task.LoadConversationTask;
import com.ezimgur.view.adapter.MessageComparator;
import com.ezimgur.view.adapter.MessagesAdapter;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import roboguice.inject.InjectView;

import java.util.Collection;
import java.util.Collections;
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
    private int convoId;
    private String from;
    private int currentPage;

    public static MessageDetailFragment newInstance(int conversationId, String from) {
        MessageDetailFragment detailFragment = new MessageDetailFragment();

        Bundle args = new Bundle();
        args.putInt("convoId", conversationId);
        args.putString("from", from);

        detailFragment.setArguments(args);

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
            convoId = args.getInt("convoId");
            from = args.getString("from");
        }

        loadMessageThread(convoId);

        attachViewListeners();
    }

    private void loadMessageThread(int convoId){
        mProgressIndicator.setVisibility(View.VISIBLE);

        new LoadConversationTask(getActivity(), convoId, currentPage){

            @Override
            protected void onException(Exception e) throws RuntimeException {
                super.onException(e);
                mProgressIndicator.setVisibility(View.GONE);
            }

            @Override
            protected void onSuccess(Conversation conversation) throws Exception {
                super.onSuccess(conversation);
                mProgressIndicator.setVisibility(View.GONE);
                Collections.sort(conversation.messages, new MessageComparator());
                setMessageThread(conversation.messages);
            }

        }.execute();
    }

    public void setMessageThread(List<Message> messages){
        mMessages = messages;
        mTxtSubject.setText("convo with " + from);
        mListThread.setAdapter(new MessagesAdapter(messages));
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
            new CreateMessageTask(getActivity(), from, reply) {

                @Override
                protected void onException(Exception e) throws RuntimeException {
                    super.onException(e);
                }

                @Override
                protected void onSuccess(Boolean aBoolean) throws Exception {
                    super.onSuccess(aBoolean);
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mTextReply.getWindowToken(), 0);
                    mTextReply.setText("");

                    loadMessageThread(convoId);
                }
            }.execute();
        }
    }

    private void deleteThread(){
        new DeleteMessageTask(getActivity(), convoId){

            @Override
            protected void onSuccess(Boolean aBoolean) throws Exception {
                super.onSuccess(aBoolean);

                getActivity().onBackPressed();
            }
        }.execute();
    }
}
