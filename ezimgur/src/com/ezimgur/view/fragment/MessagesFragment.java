package com.ezimgur.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.ezimgur.R;
import com.ezimgur.app.EzApplication;
import com.ezimgur.datacontract.Conversation;
import com.ezimgur.datacontract.Message;
import com.ezimgur.task.LoadConversationsTask;
import com.ezimgur.task.LoadMessagesTask;
import com.ezimgur.view.adapter.ConversationsAdapter;
import com.ezimgur.view.adapter.MessagesAdapter;
import com.ezimgur.view.event.OpenMessageDetailEvent;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import roboguice.event.EventManager;
import roboguice.inject.InjectView;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by NCR Corporation.
 * User: matthewharris
 * Date: 5/25/13
 * Time: 11:08 AM
 */
public class MessagesFragment extends RoboSherlockFragment {

    @InjectView(R.id.frag_msg_progress)ProgressBar mProgressIndicator;
    @InjectView(R.id.frag_msg_txt_status)TextView mTxtStatus;
    @InjectView(R.id.frag_msg_lv_messages)ListView mListMessages;

    @Inject EventManager mEventManager;

    private ConversationsAdapter conversationsAdapter;

    public static MessagesFragment newInstance(CommunityFragmentAdapter.OnMessagesFragmentChangedListener listener){
        MessagesFragment fragment = new MessagesFragment();
        return fragment;
    }

    public MessagesFragment (){
        //setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_messages, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (EzApplication.hasToken()){
            mTxtStatus.setText("No Messages");
            loadMessages();
        } else
            mTxtStatus.setText("Login to see messages");
    }

    private void loadMessages(){
        mProgressIndicator.setVisibility(View.VISIBLE);
        mTxtStatus.setVisibility(View.GONE);
        new LoadConversationsTask(getActivity()){

            @Override
            protected void onSuccess(List<Conversation> conversations) throws Exception {
                super.onSuccess(conversations);

                setMessages(conversations);
                mProgressIndicator.setVisibility(View.GONE);
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                super.onException(e);

                mTxtStatus.setVisibility(View.VISIBLE);
                mTxtStatus.setText("Unable to load messages. Error talking to imgur.");
                mProgressIndicator.setVisibility(View.GONE);
            }
        }.execute();
    }

    public void setMessages(List<Conversation> conversations){
        if (conversations != null && conversations.size() > 0){
            mTxtStatus.setVisibility(View.GONE);

            if (conversationsAdapter == null) {
                conversationsAdapter = new ConversationsAdapter(conversations);
                mListMessages.setAdapter(conversationsAdapter);
            } else {
                conversations.addAll(conversations);
            }

            attachViewListener();
        } else {
            mTxtStatus.setVisibility(View.VISIBLE);
            mTxtStatus.setText("You have no messages.");
        }
    }

    private void attachViewListener(){
        mListMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Conversation conversation = conversationsAdapter.getItem(i);
                mEventManager.fire(new OpenMessageDetailEvent(conversation.id, conversation.withAccountUsername));
            }
        });
    }


}
