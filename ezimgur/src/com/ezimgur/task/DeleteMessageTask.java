package com.ezimgur.task;

import android.content.Context;
import com.ezimgur.api.MessageApi;

import javax.inject.Inject;

/**
 * Created by NCR Corporation.
 * User: matthewharris
 * Date: 5/25/13
 * Time: 10:52 PM
 */
public class DeleteMessageTask extends LoadingTask<Boolean> {

    @Inject MessageApi mMessageApi;
    private int mId;

    public DeleteMessageTask(Context context, int id) {
        super(context);
        mId = id;
    }

    @Override
    public Boolean call() throws Exception {
        mMessageApi.deleteMessage(mId);
        return true;
    }
}
