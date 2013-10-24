package com.ezimgur.serializer;

import com.ezimgur.datacontract.GalleryItem;
import com.ezimgur.datacontract.NotificationContent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/15/12
 * Time: 12:25 AM
 */
public class GsonUtils {

    private static Gson gson;

    public static Gson getGsonInstance() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .registerTypeAdapter(Date.class, new DateTimeSerializer())
                    .registerTypeAdapter(Date.class, new DateTimeDeserializer())
                    .registerTypeAdapter(GalleryItem.class, new GalleryItemDeserializer())
                    .registerTypeAdapter(NotificationContent.class, new NotificationSerializer())
                    //.registerTypeAdapter(Basic.class, new BasicDeserializer())
                    .create();
        }

        return gson;
    }
}
