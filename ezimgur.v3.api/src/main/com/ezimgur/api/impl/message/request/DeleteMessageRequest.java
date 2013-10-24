package com.ezimgur.api.impl.message.request;

import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.http.ApiGetRequest;
import com.ezimgur.datacontract.Basic;

/**
 * Created by NCR Corporation.
 * User: matthewharris
 * Date: 5/25/13
 * Time: 10:48 PM
 */
public class DeleteMessageRequest extends ApiGetRequest<Basic> {

    private int messageId;

    public DeleteMessageRequest(int messageId) {
        this.messageId = messageId;
    }

    @Override
    public String getRequestUrl() {
        return String.format(ImgurApiConstants.URL_MESSAGE_DELETE, messageId);
    }

    @Override
    protected Class<Basic> getResponseClass() {
        return Basic.class;
    }

    @Override
    public RequestMethod getRequestMethod() {
        return RequestMethod.DELETE;
    }
}
