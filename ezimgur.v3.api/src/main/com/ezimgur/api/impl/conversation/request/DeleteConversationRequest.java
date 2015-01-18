package com.ezimgur.api.impl.conversation.request;

import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.http.ApiDeleteRequest;
import com.ezimgur.api.http.ApiRequest;
import com.ezimgur.datacontract.Basic;

/**
 * Created by mharris on 1/16/15.
 *
 */
public class DeleteConversationRequest extends ApiDeleteRequest<Basic> {

    private int conversationId;

    public DeleteConversationRequest(int conversationId) {
        this.conversationId = conversationId;
    }

    @Override
    public String getRequestUrl() {
        return String.format(ImgurApiConstants.URL_CONVERSATION_DELETE, this.conversationId);
    }

    @Override
    protected Class<Basic> getResponseClass() {
        return Basic.class;
    }
}
