package com.ezimgur.serializer;

import com.ezimgur.datacontract.Basic;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/15/12
 * Time: 12:31 AM
 */
public class BasicDeserializer implements JsonDeserializer<Basic> {

    @Override
    public Basic deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Basic basic = new Basic();
        basic.status = jsonObject.get("status").getAsInt();
        basic.success = jsonObject.get("success").getAsBoolean();

        return basic;
    }
}
