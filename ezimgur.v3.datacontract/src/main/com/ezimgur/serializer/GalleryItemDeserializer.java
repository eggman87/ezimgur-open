package com.ezimgur.serializer;

import com.ezimgur.datacontract.GalleryAlbum;
import com.ezimgur.datacontract.GalleryImage;
import com.ezimgur.datacontract.GalleryItem;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/15/12
 * Time: 12:18 AM
 */
public class GalleryItemDeserializer implements JsonDeserializer<GalleryItem> {
    @Override
    public GalleryItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject object = json.getAsJsonObject();
        if (    object != null &&
                object.get("is_album") != null &&
                object.get("is_album").getAsBoolean()) {
            return GsonUtils.getGsonInstance().fromJson(json, GalleryAlbum.class);
        } else {
            return GsonUtils.getGsonInstance().fromJson(json, GalleryImage.class);
        }

    }
}
