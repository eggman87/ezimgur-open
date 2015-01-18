package com.ezimgur.task;

import android.content.Context;
import com.ezimgur.api.ConversationApi;
import com.ezimgur.datacontract.Conversation;

import javax.inject.Inject;

/**
 * Created by mharris on 1/17/15.
 *
 */
public class LoadConversationTask extends LoadingTask<Conversation> {

    @Inject
    ConversationApi conversationApi;

    private int id;
    private int page;

    protected LoadConversationTask(Context context, int id, int page) {
        super(context);

        this.id = id;
        this.page = page;
    }

    @Override
    public Conversation call() throws Exception {
        return conversationApi.getConversation(id, page);
    }
}
