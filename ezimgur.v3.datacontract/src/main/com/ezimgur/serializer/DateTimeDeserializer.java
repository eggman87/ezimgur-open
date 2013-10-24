package com.ezimgur.serializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/14/12
 * Time: 7:24 PM
 */
public class DateTimeDeserializer implements JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return new Date(json.getAsJsonPrimitive().getAsLong() * 1000);
        } catch (NumberFormatException ex) {
            return new Date(json.getAsJsonPrimitive().getAsString());
        }
    }
}
