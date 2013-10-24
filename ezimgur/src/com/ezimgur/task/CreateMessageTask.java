package com.ezimgur.task;

import android.content.Context;
import com.ezimgur.api.MessageApi;
import com.google.inject.Inject;

/**
 * Created by NCR Corporation.
 * User: matthewharris
 * Date: 5/25/13
 * Time: 9:01 PM
 */
public class CreateMessageTask extends LoadingTask<Boolean> {

    @Inject MessageApi mMessageApi;

    private String mRecipient;
    private String mSubject;
    private String mBody;
    private Integer mParentId;

    public CreateMessageTask(Context context, String recipient, String subject, String body, Integer parentId) {
        super(context);
        mRecipient = recipient;
        mSubject = subject;
        mBody = body;
        mParentId = parentId;
    }

    @Override
    public Boolean call() throws Exception {

        mMessageApi.createNewMessage(mRecipient, mBody, mSubject, mParentId);
        return true;
    }
}
