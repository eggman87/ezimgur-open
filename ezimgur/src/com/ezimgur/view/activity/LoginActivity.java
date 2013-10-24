package com.ezimgur.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import com.ezimgur.api.exception.ApiException;
import com.ezimgur.api.impl.authentication.AuthenticationApiImpl;
import com.ezimgur.app.EzApplication;
import com.ezimgur.datacontract.AuthenticationToken;
import com.ezimgur.datacontract.OAuthRequestType;
import com.ezimgur.instrumentation.Log;
import roboguice.activity.RoboActivity;
import roboguice.util.SafeAsyncTask;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 11/30/12
 * Time: 10:25 PM
 */
public class LoginActivity extends RoboActivity {

    public static final String	OAUTH_CALLBACK_URL		= "oauthflow-imgur://callback";
    public static boolean isLoggingIn = false;
    public static boolean isLoggedIn = false;
    private String authUrl;

    private static final String TAG = "EzImgur.AccountLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.type = Log.Type.LOG4J;
        Log.d(TAG, "onCreate logginIn = "+isLoggingIn + " isLoggedIn = " + isLoggedIn);

        if (!EzApplication.isAuthenticated() && !isLoggingIn ) {
            isLoggingIn = false;
            showLoginPrompt();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume logginIn = "+isLoggingIn + " isLoggedIn = " + isLoggedIn);

        if (isLoggingIn) {
            final Uri uri = getIntent().getData();
            final Activity act = this;

            if (uri != null) {
                String uriPath = uri.toString();
                if (uriPath.startsWith(OAUTH_CALLBACK_URL))
                {
                    new SafeAsyncTask<AuthenticationToken>() {

                        @Override
                        public AuthenticationToken call() throws Exception {
                            if (!isLoggedIn) {
                                return new AuthenticationApiImpl().getTokenFromTokenResponse(uri.getFragment());
                            }

                            return null;
                        }

                        @Override
                        protected void onSuccess(AuthenticationToken token) throws Exception {
                            super.onSuccess(token);
                            //Toast.makeText(getBaseContext(), "got a token " + token.accessToken, Toast.LENGTH_LONG).show();
                            Log.d("EzImgur", "token recieved! " + token.accessToken);

                            EzApplication.setAuthenticationToken(getBaseContext(), token);
                        }

                        @Override
                        protected void onException(Exception e) throws RuntimeException {
                            super.onException(e);
                            Toast.makeText(getBaseContext(), "exception trying to get token ", Toast.LENGTH_LONG).show();
                            Log.d("EzImgur", "error getting token", e);
                        }

                        @Override
                        protected void onFinally() throws RuntimeException {
                            super.onFinally();
                            isLoggingIn = false;
                            isLoggedIn = false;
                            Log.d(TAG, "finished logging in - killing activity ");
                            act.finish();
                        }
                    }.execute();
                }
                else {
                    isLoggedIn = false;
                    isLoggingIn = false;
                }
            } else  {
                showLoginPrompt();
            }
        }

    }

    private void showLoginPrompt() {

            try {
                authUrl = new AuthenticationApiImpl().getOAuthUrl(OAUTH_CALLBACK_URL, OAuthRequestType.TOKEN);
            } catch (ApiException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "SHOWING LOGIN PROMPT YOU FUCK");
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl));
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.addFlags(Intent.FLAG_FROM_BACKGROUND);
            startActivityForResult(intent, 0);
            isLoggingIn = true;
        }
}
