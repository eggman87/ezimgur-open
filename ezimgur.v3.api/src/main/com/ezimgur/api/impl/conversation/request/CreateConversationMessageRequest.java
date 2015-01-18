package com.ezimgur.api.impl.conversation.request;

import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.http.ApiRequest;
import com.ezimgur.datacontract.Basic;

/**
 * Created by mharris on 1/16/15.
 *
 */
public class CreateConversationMessageRequest extends ApiRequest<CreateMessagePayload, Basic> {

    public CreateConversationMessageRequest(String recipient, String message) {
        this.itemToSend = new CreateMessagePayload();
        this.itemToSend.recipient = recipient;
        this.itemToSend.body = message;
    }

    @Override
    public String getRequestUrl() {
        return String.format(ImgurApiConstants.URL_CONVERSATION_CREATE, this.itemToSend.recipient);
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
}
