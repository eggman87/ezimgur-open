package com.ezimgur.task;

import android.content.Context;
import com.ezimgur.api.ConversationApi;
import com.ezimgur.api.MessageApi;

import javax.inject.Inject;

/**
 * Created by NCR Corporation.
 * User: matthewharris
 * Date: 5/25/13
 * Time: 10:52 PM
 */
public class DeleteMessageTask extends LoadingTask<Boolean> {

    @Inject
    ConversationApi conversationApi;

    private int conversationId;

    public DeleteMessageTask(Context context, int id) {
        super(context);

        this.conversationId = conversationId;
    }

    @Override
    public Boolean call() throws Exception {
        conversationApi.deleteConversation(conversationId);
        return true;
    }
}
