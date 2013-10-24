package com.ezimgur.task;

import android.content.Context;
import com.ezimgur.api.AuthenticationApi;
import com.ezimgur.datacontract.AuthenticationToken;
import com.google.inject.Inject;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/21/12
 * Time: 6:56 PM
 */
public class RefreshTokenTask extends LoadingTask <AuthenticationToken> {

    @Inject AuthenticationApi mAuthenticationApi;
    private String mRefreshToken;

    public RefreshTokenTask(Context context, String refreshToken) {
        super(context);

        mRefreshToken = refreshToken;
    }

    @Override
    public AuthenticationToken call() throws Exception {
        return mAuthenticationApi.refreshAuthenticationToken(mRefreshToken);
    }

    @Override
    protected void onSuccess(AuthenticationToken authenticationToken) throws Exception {
        super.onSuccess(authenticationToken);

        mAuthenticationApi.setCurrentAuthenticationToken(authenticationToken);
    }
}
