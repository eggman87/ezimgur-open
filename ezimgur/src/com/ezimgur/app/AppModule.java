package com.ezimgur.app;

import com.ezimgur.api.*;
import com.ezimgur.api.impl.account.AccountApiImpl;
import com.ezimgur.api.impl.album.AlbumApiImpl;
import com.ezimgur.api.impl.authentication.AuthenticationApiImpl;
import com.ezimgur.api.impl.comment.CommentApiImpl;
import com.ezimgur.api.impl.gallery.GalleryApiImpl;
import com.ezimgur.api.impl.image.ImageApiImpl;
import com.ezimgur.api.impl.message.MessageApiImpl;
import com.ezimgur.api.impl.notification.NotificationApiImpl;
import com.ezimgur.control.GalleryController;
import com.ezimgur.control.IGalleryController;
import com.ezimgur.instrumentation.Log;
import com.google.inject.AbstractModule;
import com.google.inject.internal.util.$FinalizableSoftReference;

/**
 * EggmanProjects
 * Author: Matthew Harris
 * Date: 9/30/12
 * Time: 1:02 PM
 */
public class AppModule extends AbstractModule {

    @Override
    protected void configure() {
        //bind(ImgurApi.class).to(ImgurApiImpl.class);

        bind(AccountApi.class).to(AccountApiImpl.class);
        bind(AlbumApi.class).to(AlbumApiImpl.class);
        bind(AuthenticationApi.class).to(AuthenticationApiImpl.class);
        bind(CommentApi.class).to(CommentApiImpl.class);
        bind(GalleryApi.class).to(GalleryApiImpl.class);
        bind(ImageApi.class).to(ImageApiImpl.class);
        bind(MessageApi.class).to(MessageApiImpl.class);
        bind(NotificationApi.class).to(NotificationApiImpl.class);


        bind(IGalleryController.class).to(GalleryController.class).asEagerSingleton();

        Log.LOG_ENABLED = false;
        Log.type = Log.Type.ANDROID;
    }
}
