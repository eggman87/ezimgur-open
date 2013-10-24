package com.ezimgur.api.impl.message.request;

import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.http.ApiRequest;
import com.ezimgur.datacontract.Basic;
import com.google.gson.annotations.SerializedName;

/**
 * Created by NCR Corporation.
 * User: matthewharris
 * Date: 5/25/13
 * Time: 8:40 PM
 */
public class CreateMessageRequest extends ApiRequest<CreateMessageRequest.CreateMessagePayload, Basic>{

    public CreateMessageRequest(String recipient, String body, String subject, Integer parentId){
        this.itemToSend = new CreateMessagePayload();
        this.itemToSend.recipient = recipient;
        this.itemToSend.body = body;
        this.itemToSend.subject = subject;
        this.itemToSend.parentId = parentId;
    }

    @Override
    public String getRequestUrl() {
        return ImgurApiConstants.URL_MESSAGE_CREATE_NEW;
    }

    @Override
    public RequestMethod getRequestMethod() {
        return RequestMethod.POST;
    }

    @Override
    protected Class<Basic> getResponseClass() {
        return Basic.class;
    }

    @Override
    protected Class<CreateMessagePayload> getRequestClass() {
        return CreateMessagePayload.class;
    }

    public class CreateMessagePayload {
        public String recipient;
        public String body;
        public String subject;
        @SerializedName("parent_id")
        public Integer parentId;
    }
}
