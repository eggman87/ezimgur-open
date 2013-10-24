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
 * Time: 6:53 PM
 */
public class GetMessageThreadTask extends LoadingTask<List<Message>> {

    @Inject MessageApi mMessageApi;

    private int mParentId;

    public GetMessageThreadTask(Context context, int parentId) {
        super(context);
        mParentId = parentId;
        mUseProgress = false;
    }

    @Override
    public List<Message> call() throws Exception {
        return mMessageApi.getMessageThreadById(mParentId);
    }
}
