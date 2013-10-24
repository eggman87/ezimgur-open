package com.ezimgur.task;

import android.content.Context;
import com.ezimgur.api.AuthenticationApi;
import com.ezimgur.app.EzApplication;
import com.ezimgur.datacontract.AuthenticationToken;
import com.google.inject.Inject;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 1/5/13
 * Time: 4:57 PM
 */
public class AuthenticateUserTask extends LoadingTask<Boolean> {

    @Inject
    protected AuthenticationApi mAuthenticationApi;

    protected AuthenticateUserTask(Context context) {
        super(context);
    }

    @Override
    public Boolean call() throws Exception {
        //make sure our access token is good to go...
        AuthenticationToken newToken = mAuthenticationApi.refreshAuthenticationToken(EzApplication.getRefreshToken());

        mAuthenticationApi.setCurrentAuthenticationToken(newToken);
        EzApplication.setAuthenticationToken(getContext(), newToken);
        return false;
    }
}
