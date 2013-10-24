package com.ezimgur.api.impl.account.response;

import com.ezimgur.datacontract.*;

import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/27/12
 * Time: 8:53 PM
 */
public class ResponseContainer {

    public class GetAccountContainer extends Basic<Account> {

    }

    public class GetAccountAlbumContainer extends Basic<List<Album>> {

    }

    public class GetAccountImagesContainer extends Basic<List<Image>> {

    }

    public class GetAccountNotificationsResponse extends Basic<NotificationResponse> {

    }

    public class GetAccountLikesResponse extends Basic<List<GalleryItem>> {

    }

    public class NotificationResponse {

        public List<Notification> replies;
        public List<Notification> messages;
    }

}
