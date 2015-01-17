package com.ezimgur.api;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/14/12
 * Time: 10:09 PM
 */
public class ImgurApiConstants {

    public static final String URL_IMAGE_FORMAT = "http://i.imgur.com/%s%s%s";
    public static final String URL_ALBUM_FORMAT = "http://imgur.com/a/%s";
    public static final String URL_IMAGE_PAGE_FORMAT = "http://imgur.com/%s/%s";

    //format args =  galleryName/sort/pagenumber
    public static final String URL_GALLERY_LOAD = "https://api.imgur.com/3/gallery/%s/%s/%s.json";
    public static final String URL_GALLERY_SEARCH = "https://api.imgur.com/3/gallery/search?q=%s";
    public static final String URL_GALLERY_ADD_ITEM = "https://api.imgur.com/3/gallery/%s";
    public static final String URL_GALLERY_LOAD_ALBUM = "https://api.imgur.com/3/gallery/album/%s";
    public static final String URL_GALLERY_LOAD_IMAGE = "https://api.imgur.com/3/gallery/image/%s";
    public static final String URL_GALLERY_REPORT_ITEM = "https://api.imgur.com/3/gallery/%s/report";
    public static final String URL_GALLERY_GET_VOTE = "https://api.imgur.com/3/gallery/%s/votes";
    public static final String URL_GALLERY_SUBMIT_VOTE = "https://api.imgur.com/3/gallery/%s/vote/%s";
    public static final String URL_GALLERY_ITEM_COMMENTS = "https://api.imgur.com/3/gallery/%s/comments";
    public static final String URL_GALLERY_ITEM_GET = "https://api.imgur.com/3/gallery/%s";

    public static final String URL_ALBUM_GET_ALBUM = "https://api.imgur.com/3/album/%s";
    public static final String URL_ALBUM_GET_IMAGES = "https://api.imgur.com/3/album/%s/images";

    public static final String URL_IMAGE_UPLOAD = "https://api.imgur.com/3/image";
    public static final String URL_IMAGE_GET = "https://api.imgur.com/3/image/%s";

    public static final String URL_COMMENT_GET = "https://api.imgur.com/3/comment/%s";
    public static final String URL_COMMENT_GET_WITH_REPLIES = "https://api.imgur.com/3/comment/%s/replies";
    public static final String URL_COMMENT_ADD = "https://api.imgur.com/3/comment";
    public static final String URL_COMMENT_ADD_GALLERY = "https://api.imgur.com/3/gallery/%s/comment";
    public static final String URL_COMMENT_DELETE = "https://api.imgur.com/3/comment/%s";
    public static final String URL_COMMENT_VOTE = "https://api.imgur.com/3/comment/%s/vote/%s";
    public static final String URL_COMMENT_REPORT = "https://api.imgur.com/3/comment/%s/report";
    public static final String URL_COMMENT_REPLY = "https://api.imgur.com/3/comment/%s";

    public static final String URL_ACCOUNT_GETIMAGES = "https://api.imgur.com/3/account/%s/images/";
    public static final String URL_ACCOUNT_GETALBUMS = "https://api.imgur.com/3/account/%s/albums/%s";
    public static final String URL_ACCOUNT_GET_NOTIFICATIONS = "https://api.imgur.com/3/account/%s/notifications?new=false";
    public static final String URL_ACCOUNT_GET_LIKES = "https://api.imgur.com/3/account/%s/likes";

    public static final String URL_FAVORITE_ITEM = "https://api.imgur.com/3/image/%s/favorite";
    public static final String URL_FAVORITE_ALBUM = "https://api.imgur.com/3/album/%s/favorite";

    public static final String URL_MESSAGE_GET_MESSAGES = "https://api.imgur.com/3/messages";
    public static final String URL_MESSAGE_GET_MESSAGE_IDS = "https://api.imgur.com/3/messages/ids/%s";
    public static final String URL_MESSAGE_GET_MESSAGE_COUNT = "https://api.imgur.com/3/messages/count";
    public static final String URL_MESSAGE_GET_BY_ID = "https://api.imgur.com/3/message/%s";
    public static final String URL_MESSAGE_GET_THREAD_BY_ID = "https://api.imgur.com/3/message/%s/thread";
    public static final String URL_MESSAGE_CREATE_NEW = "https://api.imgur.com/3/message";
    public static final String URL_MESSAGE_DELETE = "https://api.imgur.com/3/message/%s";

    public static final String URL_CONVERSATION_GET_ALL = "https://api.imgur.com/3/conversations";
    //id, page, offset
    public static final String URL_CONVERSATION_GET = "https://api.imgur.com/3/conversations/%s/%s/%s";
    public static final String URL_CONVERSATION_DELETE = "https://api.imgur.com/3/conversations/%s";
    public static final String URL_CONVERSATION_CREATE = "https://api.imgur.com/3/conversations/%s";

    public static final String CLIENT_ID = "ASK_EGGMAN_FOR_KEY";
    public static final String CLIENT_SECRET = "ASK_EGGMAN_FOR_KEY";
    public static final String OAUTH_URL = "https://api.imgur.com/oauth2/authorize?client_id=%s&redirect_uri=%s&response_type=%s";
    public static final String OAUTH_REFRESH_TOKEN_URL = "https://api.imgur.com/oauth2/token";

    public static final String HEADER_CLIENT_AUTH = "Authorization";
    public static final String HEADER_CLIENT_AUTH_PREFIX = "Client-ID ";
    public static final String HEADER_CLIENT_USER_AUTH_PREFIX = "Bearer ";
}
