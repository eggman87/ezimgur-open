package com.ezimgur.task;

import android.content.Context;
import com.ezimgur.api.MessageApi;
import com.ezimgur.datacontract.Message;
import com.google.inject.Inject;

import java.util.List;

/**
 * Created by NCR Corporation.
 * User: matthewharris
 * Date: 5/25/13
 * Time: 11:49 AM
 */
public class LoadMessagesTask extends LoadingTask<List<Message>> {

    @Inject
    MessageApi mMessageApi;

    protected LoadMessagesTask(Context context) {
        super(context);
        mUseProgress = false;
    }

    @Override
    public List<Message> call() throws Exception {
        return mMessageApi.getMessages();
    }
}
