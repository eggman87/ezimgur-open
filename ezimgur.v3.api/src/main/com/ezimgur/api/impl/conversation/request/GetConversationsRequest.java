package com.ezimgur.api.impl.conversation.request;

import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.http.ApiGetRequest;
import com.ezimgur.datacontract.Basic;
import com.ezimgur.datacontract.Conversation;

import java.util.List;

/**
 * Created by mharris on 1/16/15.
 *
 */
public class GetConversationsRequest extends ApiGetRequest<GetConversationsRequest.GetConversationsResponse> {

    @Override
    public String getRequestUrl() {
        return ImgurApiConstants.URL_CONVERSATION_GET_ALL;
    }

    @Override
    protected Class<GetConversationsResponse> getResponseClass() {
        return GetConversationsResponse.class;
    }

    public class GetConversationsResponse extends Basic<List<Conversation>> {

    }
}
