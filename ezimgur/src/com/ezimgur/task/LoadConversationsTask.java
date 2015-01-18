package com.ezimgur.task;

import android.content.Context;
import com.ezimgur.api.ConversationApi;
import com.ezimgur.api.MessageApi;
import com.ezimgur.datacontract.Conversation;
import com.ezimgur.datacontract.Message;
import com.google.inject.Inject;

import java.util.List;

/**
 * Created by mharris on 1/16/15.
 *
 *
 */
public class LoadConversationsTask extends LoadingTask<List<Conversation>>{
    @Inject
    ConversationApi conversationApi;

    protected LoadConversationsTask(Context context) {
        super(context);
        mUseProgress = false;
    }

    @Override
    public List<Conversation> call() throws Exception {
        return conversationApi.getConversations();
    }
}
