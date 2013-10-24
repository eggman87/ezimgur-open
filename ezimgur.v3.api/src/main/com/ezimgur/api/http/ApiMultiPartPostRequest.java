package com.ezimgur.api.http;

import com.ezimgur.datacontract.Basic;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/27/12
 * Time: 10:03 PM
 */
public abstract class ApiMultiPartPostRequest<TReq extends BigMultiPartEntity, TRes extends Basic> extends ApiRequest<TReq, TRes>{

    public ApiMultiPartPostRequest(TReq requestItem) {
        super(requestItem);
    }

    @Override
    public RequestMethod getRequestMethod() {
        return RequestMethod.POST;
    }

    @Override
    protected Class<TReq> getRequestClass() {
        //not used for this request type...
        return null;
    }

}
