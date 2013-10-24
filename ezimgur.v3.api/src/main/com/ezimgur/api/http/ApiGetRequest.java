package com.ezimgur.api.http;

import com.ezimgur.datacontract.Basic;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/15/12
 * Time: 6:11 PM
 */
public abstract class ApiGetRequest <TRes extends Basic> extends ApiRequest<Object, TRes> {

    @Override
    public RequestMethod getRequestMethod() {
        return RequestMethod.GET;
    }

    @Override
    protected Class<Object> getRequestClass() {
        return Object.class;
    }
}
