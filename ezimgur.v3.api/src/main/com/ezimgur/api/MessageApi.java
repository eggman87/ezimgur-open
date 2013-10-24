package com.ezimgur.api;

import com.ezimgur.api.exception.ApiException;
import com.ezimgur.datacontract.Message;

import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/15/12
 * Time: 4:00 PM
 */
public interface MessageApi {

    /**
     * Get one message from everythread the user has received. For example if a user has 5 messages, but 3 of them
     * are on the same thread there will be 3 messages returned.
     * @return
     */
    List<Message> getMessages() throws ApiException;

    /**
     * returns an array of the message ids, this also limited and is paged.
     * @param page
     * @return
     */
    List<String> getMessageIds(int page) throws ApiException;

    /**
     * Return the total count of messages for the user.
     * @return
     */
    int getTotalMessageCount() throws ApiException;

    /**
     * Get information about a specific message
     * @param id
     * @return
     */
    Message getMessageById(String id) throws ApiException;

    /**
     * Get a specific message and the rest of the conversation with that message. The message ID sent needs to be the parent id. This is the same id as the first message in the thread.
     * @param parentId
     * @return
     */
    List<Message> getMessageThreadById(int parentId) throws ApiException;

    /**
     * Create a new message.
     * @param recipient The recipient username, this person will receive the message
     * @param body The message itself, similar to the body of an email.
     * @param subject The subject of the message, this is not required
     * @param parentId If this message is in reply to another message set the parent_id to message id of the initial message.
     * @throws ApiException
     */
    void createNewMessage(String recipient, String body, String subject, Integer parentId) throws ApiException;

    /**
     * Delete a message by the given ID.
     * @param id the id of the message to delete.
     */
    void deleteMessage(int id) throws ApiException;

    /**
     * Report a user for sending messages that are against the Terms of Service.
     * @param offensiveUserName
     * @throws ApiException
     */
    void reportSender(String offensiveUserName) throws ApiException;

    /**
     * Block the user from sending messages to the user that is logged in.
     * @param userNameToBlock
     * @throws ApiException
     */
    void blockUser(String userNameToBlock) throws ApiException;


}
