package com.ezimgur.api.impl.message.request;

import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.http.ApiGetRequest;
import com.ezimgur.datacontract.Basic;
import com.ezimgur.datacontract.Message;

import java.util.List;

/**
 * Created by NCR Corporation.
 * User: matthewharris
 * Date: 5/25/13
 * Time: 6:46 PM
 */
public class GetMessageThreadRequest extends ApiGetRequest<GetMessageThreadRequest.GetMessageThreadResponse>{

    private int id;

    public GetMessageThreadRequest(int id) {
        this.id = id;
    }

    @Override
    public String getRequestUrl() {
        return String.format(ImgurApiConstants.URL_MESSAGE_GET_THREAD_BY_ID, this.id);
    }

    @Override
    protected Class<GetMessageThreadResponse> getResponseClass() {
        return GetMessageThreadResponse.class;
    }

    public class GetMessageThreadResponse extends Basic<List<Message>> {

    }
}
