package com.ezimgur.api.http;

import com.ezimgur.datacontract.Basic;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/16/12
 * Time: 4:36 PM
 */
public abstract class ApiDeleteRequest <TRes extends Basic> extends ApiRequest<Object, TRes> {

    @Override
    public RequestMethod getRequestMethod() {
        return RequestMethod.DELETE;
    }

    @Override
    protected Class<Object> getRequestClass() {
        return Object.class;
    }
}
