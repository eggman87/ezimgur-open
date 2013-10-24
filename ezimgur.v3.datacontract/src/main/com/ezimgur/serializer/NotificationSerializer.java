package com.ezimgur.serializer;

import com.ezimgur.datacontract.Comment;
import com.ezimgur.datacontract.Message;
import com.ezimgur.datacontract.NotificationContent;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/28/12
 * Time: 10:05 PM
 */
public class NotificationSerializer implements JsonDeserializer<NotificationContent>, JsonSerializer<NotificationContent> {

    @Override
    public NotificationContent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.getAsJsonObject().get("comment") != null) {
            return GsonUtils.getGsonInstance().fromJson(json, Comment.class);
        } else {
            return GsonUtils.getGsonInstance().fromJson(json, Message.class);
        }
    }

    @Override
    public JsonElement serialize(NotificationContent src, Type typeOfSrc, JsonSerializationContext context) {

        JsonParser parser = new JsonParser();
        String json = "";
        if (src instanceof Comment) {
            json = GsonUtils.getGsonInstance().toJson(src, Comment.class);
        } else if (src instanceof Message) {
            json = GsonUtils.getGsonInstance().toJson(src, Message.class);
        }

        return (JsonObject) parser.parse(json);
    }
}
