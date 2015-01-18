package com.ezimgur.api.impl.conversation;

import com.ezimgur.api.ConversationApi;
import com.ezimgur.api.exception.ApiException;
import com.ezimgur.api.impl.ApiBase;
import com.ezimgur.api.impl.conversation.request.CreateConversationMessageRequest;
import com.ezimgur.api.impl.conversation.request.DeleteConversationRequest;
import com.ezimgur.api.impl.conversation.request.GetConversationRequest;
import com.ezimgur.api.impl.conversation.request.GetConversationsRequest;
import com.ezimgur.datacontract.Conversation;

import java.util.List;

/**
 * Created by mharris on 1/16/15.
 *
 */

public class ConversationApiImpl extends ApiBase implements ConversationApi {

    @Override
    public List<Conversation> getConversations() throws ApiException {
        GetConversationsRequest request = new GetConversationsRequest();
        submitApiRequest(request);

        return request.getItemToReceive().data;
    }

    @Override
    public Conversation getConversation(int conversationId, int page) throws ApiException  {
        GetConversationRequest request = new GetConversationRequest(conversationId, page);

        submitApiRequest(request);

        return request.getItemToReceive().data;
    }

    @Override
    public void sendMessage(String reciepient, String message) throws ApiException {
        CreateConversationMessageRequest request = new CreateConversationMessageRequest(reciepient, message);

        submitApiRequest(request);
    }

    @Override
    public void deleteConversation(int conversationId) throws ApiException {
        DeleteConversationRequest request = new DeleteConversationRequest(conversationId);

        submitApiRequest(request);
    }
}
