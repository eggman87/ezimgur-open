package com.ezimgur.api.impl.account.request;

import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.http.ApiGetRequest;
import com.ezimgur.api.impl.account.response.ResponseContainer;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/28/12
 * Time: 9:15 PM
 */
public class GetAccountNotificationsRequest extends ApiGetRequest<ResponseContainer.GetAccountNotificationsResponse> {

    private String userName;

    public GetAccountNotificationsRequest(String userName) {
        this.userName = userName;
    }

    @Override
    public String getRequestUrl() {
        return String.format(ImgurApiConstants.URL_ACCOUNT_GET_NOTIFICATIONS, userName);
    }

    @Override
    protected Class<ResponseContainer.GetAccountNotificationsResponse> getResponseClass() {
        return ResponseContainer.GetAccountNotificationsResponse.class;
    }
}
