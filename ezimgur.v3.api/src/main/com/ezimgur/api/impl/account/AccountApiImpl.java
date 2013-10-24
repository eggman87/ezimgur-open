package com.ezimgur.api.impl.account;

import com.ezimgur.api.AccountApi;
import com.ezimgur.api.exception.ApiException;
import com.ezimgur.api.impl.ApiBase;
import com.ezimgur.api.impl.account.request.GetAccountAlbumsRequest;
import com.ezimgur.api.impl.account.request.GetAccountImagesRequest;
import com.ezimgur.api.impl.account.request.GetAccountLikesRequest;
import com.ezimgur.api.impl.account.request.GetAccountNotificationsRequest;
import com.ezimgur.datacontract.*;

import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/15/12
 * Time: 3:58 PM
 */
public class AccountApiImpl extends ApiBase implements AccountApi {

    @Override
    public Account getAccount(String username) {
        return null;
    }

    @Override
    public List<GalleryItem> getLikedItems(String username) throws ApiException{

        GetAccountLikesRequest request = new GetAccountLikesRequest(username);
        submitApiRequest(request);

        return request.getItemToReceive().data;
    }

    @Override
    public List<Image> getSubmittedImages(String username, int page) {
        return null;
    }

    @Override
    public AccountSettings getAccountSettings(String userName) {
        return null;
    }

    @Override
    public AccountStats getAccountStats(String username) {
        return null;
    }

    @Override
    public GalleryTotals getAccountTotals(String username) {
        return null;
    }

    @Override
    public List<Album> getAccountAlbums(String username, int page) throws ApiException{

        GetAccountAlbumsRequest request = new GetAccountAlbumsRequest(username, page);
        submitApiRequest(request);

        return request.getItemToReceive().data;
    }

    @Override
    public List<String> getAccountAlbumIds(String username) {
        return null;
    }

    @Override
    public int getAccountAlbumCount(String username) {
        return 0;
    }

    @Override
    public boolean deleteAlbum(String albumId) {
        return false;
    }

    @Override
    public List<Comment> getCommentsForAccount(String username) {
        return null;
    }

    @Override
    public List<String> getCommentIdsForAccount(String username) {
        return null;
    }

    @Override
    public int getCommentCountForAccount(String username) {
        return 0;
    }

    @Override
    public boolean deleteCommentForCurrentAccount() {
        return false;
    }

    @Override
    public List<Image> getAssociatedImages(String username, int page) throws ApiException {

        GetAccountImagesRequest request = new GetAccountImagesRequest(username, page);
        submitApiRequest(request);

        return request.getItemToReceive().data;
    }

    @Override
    public List<String> getAssociatedImageIds(String username) {
        return null;
    }

    @Override
    public int getAssociatedImageCount(String username) {
        return 0;
    }

    @Override
    public boolean deleteImage(String deleteHash) {
        return false;
    }

    @Override
    public List<Notification> getAccountNotifications() throws ApiException{

        GetAccountNotificationsRequest request = new GetAccountNotificationsRequest("me");
        submitApiRequest(request);

        List<Notification> allNotes;
        allNotes = request.getItemToReceive().data.messages;
        allNotes.addAll(request.getItemToReceive().data.replies);

        return allNotes;
    }

    @Override
    public List<Message> getAccountMessages() {
        return null;
    }

    @Override
    public boolean sendMessageToUser(String toUsername, Message message) {
        return false;
    }

    @Override
    public List<Notification> getReplyNotifications() {
        return null;
    }
}
