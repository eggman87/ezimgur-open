package com.ezimgur.api.impl.message.request;

import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.http.ApiGetRequest;
import com.ezimgur.datacontract.Basic;
import com.ezimgur.datacontract.Message;

import java.util.List;

/**
 * Created by NCR Corporation.
 * User: matthewharris
 * Date: 5/24/13
 * Time: 9:40 PM
 */
public class GetMessagesRequest extends ApiGetRequest<GetMessagesRequest.GetMessagesResponse> {


    @Override
    public String getRequestUrl() {
        return ImgurApiConstants.URL_MESSAGE_GET_MESSAGES;
    }

    @Override
    protected Class<GetMessagesResponse> getResponseClass() {
        return GetMessagesResponse.class;
    }

    public class GetMessagesResponse extends Basic<List<Message>> {

    }
}
