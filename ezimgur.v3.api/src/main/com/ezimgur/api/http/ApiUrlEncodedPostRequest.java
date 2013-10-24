package com.ezimgur.api.http;

import com.ezimgur.datacontract.Basic;
import org.apache.http.NameValuePair;

import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/18/12
 * Time: 8:31 PM
 */
public abstract class ApiUrlEncodedPostRequest <TRes extends Basic> extends ApiRequest<Object, TRes> {

    @Override
    protected Class<Object> getRequestClass() {
        return Object.class;

    }

    public abstract List<NameValuePair> getUrlEncodedEntityValues();
}
