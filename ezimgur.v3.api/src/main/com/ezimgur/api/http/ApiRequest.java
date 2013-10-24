package com.ezimgur.api.http;

import com.ezimgur.datacontract.Basic;
import com.ezimgur.instrumentation.Log;
import com.ezimgur.serializer.GsonUtils;
import org.json.JSONException;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/14/12
 * Time: 9:31 PM
 */
public abstract class ApiRequest <TReq, TRes extends Basic> {

    private static final String TAG = "EzImgur.ApiRequest";
    protected TReq itemToSend;
    protected TRes itemToReceive;
    protected ResponseError error;
    private int statusCode;

    public ApiRequest() {
    }

    public ApiRequest(TReq itemToSend) {
        this.itemToSend = itemToSend;
    }

    public TRes getItemToReceive() {
        return itemToReceive;
    }

    public void deserializeResponseString(String jsonData) throws JSONException {
        itemToReceive = GsonUtils.getGsonInstance().fromJson(jsonData, getResponseClass());
    }

    public void deserializeResponseError(String jsonData, int httpStatusCode) throws JSONException {
        statusCode = httpStatusCode;
        try {
            error = GsonUtils.getGsonInstance().fromJson(jsonData, ResponseError.ResponseErrorContainer.class).data;
        } catch (Exception e) {
            Log.e(TAG, "unable to deserialize error response from imgur.");
        }
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String serializeRequest() {
        if (itemToSend != null)
            return GsonUtils.getGsonInstance().toJson(itemToSend, getRequestClass());
        return "";
    }

    public ResponseError getResponseError() {
        return error;
    }

    public enum RequestMethod {
        GET, POST, PUT, DELETE
    }

    public boolean isSuccessful() {
        if (itemToReceive == null)
            return false;
        return itemToReceive.success;
    }

    public abstract String getRequestUrl();
    public abstract RequestMethod getRequestMethod();
    protected abstract Class<TRes> getResponseClass();
    protected abstract Class<TReq> getRequestClass();
}
