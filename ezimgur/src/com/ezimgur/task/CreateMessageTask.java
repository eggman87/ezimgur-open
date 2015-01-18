package com.ezimgur.task;

import android.content.Context;
import com.ezimgur.api.ConversationApi;
import com.ezimgur.api.MessageApi;
import com.google.inject.Inject;

/**
 * Created by NCR Corporation.
 * User: matthewharris
 * Date: 5/25/13
 * Time: 9:01 PM
 */
public class CreateMessageTask extends LoadingTask<Boolean> {

    @Inject
    protected ConversationApi conversationApi;

    private String recipient;
    private String message;

    public CreateMessageTask(Context context, String recipient, String message) {
        super(context);
        this.recipient = recipient;
        this.message = message;
    }

    @Override
    public Boolean call() throws Exception {
        conversationApi.sendMessage(recipient, message);
        return true;
    }
}
