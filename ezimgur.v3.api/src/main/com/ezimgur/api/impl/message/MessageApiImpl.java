package com.ezimgur.api.impl.message;

import com.ezimgur.api.MessageApi;
import com.ezimgur.api.exception.ApiException;
import com.ezimgur.api.impl.ApiBase;
import com.ezimgur.api.impl.message.request.CreateMessageRequest;
import com.ezimgur.api.impl.message.request.DeleteMessageRequest;
import com.ezimgur.api.impl.message.request.GetMessageThreadRequest;
import com.ezimgur.api.impl.message.request.GetMessagesRequest;
import com.ezimgur.datacontract.Message;

import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/16/12
 * Time: 7:35 PM
 */
public class MessageApiImpl extends ApiBase implements MessageApi {
    @Override
    public List<Message> getMessages() throws ApiException {
        GetMessagesRequest request = new GetMessagesRequest();
        submitApiRequest(request);

        return request.getItemToReceive().data;
    }

    @Override
    public List<String> getMessageIds(int page) throws ApiException {
        return null;
    }

    @Override
    public int getTotalMessageCount() throws ApiException {
        return 0;
    }

    @Override
    public Message getMessageById(String id) throws ApiException {
        return null;
    }

    @Override
    public List<Message> getMessageThreadById(int parentId) throws ApiException {
        GetMessageThreadRequest request = new GetMessageThreadRequest(parentId);

        submitApiRequest(request);

        return request.getItemToReceive().data;
    }

    @Override
    public void createNewMessage(String recipient, String body, String subject, Integer parentId) throws ApiException {
        CreateMessageRequest request = new CreateMessageRequest(recipient, body, subject, parentId);

        submitApiRequest(request);
    }

    @Override
    public void deleteMessage(int id) throws ApiException{
        DeleteMessageRequest request = new DeleteMessageRequest(id);

        submitApiRequest(request);
    }

    @Override
    public void reportSender(String offensiveUserName) throws ApiException {

    }

    @Override
    public void blockUser(String userNameToBlock) throws ApiException {

    }
}
