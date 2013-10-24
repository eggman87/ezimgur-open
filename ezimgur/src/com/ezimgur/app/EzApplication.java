package com.ezimgur.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;
import com.ezimgur.datacontract.Account;
import com.ezimgur.datacontract.AuthenticationToken;
import com.ezimgur.instrumentation.Log;

/**
 * EggmanProjects
 * Author: Matthew Harris
 * Date: 10/1/12
 * Time: 10:45 AM
 */
public class EzApplication extends Application {

    private static Account mAccount;
    private static AuthenticationToken mToken;
    public static boolean initialized = false;
    public static final String versionKey = "2.0";
    private static final String TAG = "EzImgur.ezapplication";

    @Override
    public void onCreate() {
        super.onCreate();

        String username = getValue(getApplicationContext(), "username", null);
        String token = getValue(getApplicationContext(), "token", null);
        String refreshToken = getValue(getApplicationContext(), "refreshtoken", null);
        String expires = getValue(getApplicationContext(), "expires", null);
        if (token != null){

            mToken = new AuthenticationToken();
            if (expires != null)
                mToken.expires = Long.parseLong(expires);
            mToken.accountUserName = username;
            mToken.accessToken = token;
            mToken.refreshToken = refreshToken;
            if (username != null) {
                mAccount = new Account();
                mAccount.url = username;
            }
        }
    }

    public static boolean isAuthenticated() {
        if (mToken != null)
            Log.d(TAG, String.format("checking if auth token is expired...current time ms=%s | authtoken expires=%s", System.currentTimeMillis() , mToken.expires));
        return mToken != null && mToken.isAuthenticated();
    }

    public static String getRefreshToken() {
        if (mToken != null)
            return mToken.refreshToken;
        return null;
    }

    public static String getAccountUserName() {
        if (mToken != null)
            return mToken.accountUserName;
        return null;
    }

    public static boolean hasToken() {
        return mToken != null;
    }

    public static Account getAccount() {
        return mAccount;
    }

    public static void setAccount(Context context, Account account) {
        mAccount =  account;
        saveValue(context, "account_url", account.url);
    }

    public static void setAuthenticationToken(Context context, AuthenticationToken token) {
        mToken = token;
        saveValue(context, "username", token.accountUserName);
        saveValue(context, "token", token.accessToken);
        saveValue(context, "refreshtoken", token.refreshToken);
        saveValue(context, "expires", String.valueOf(token.expires));

        mAccount = new Account();
        mAccount.url = token.accountUserName;
    }

    public static void logoutUser(Context context) {
        mToken = null;
        saveValue(context, "token", null);
    }

    public static boolean hasBeenInitialized(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String versionInitAs = pref.getString("version_init", null);
        if (versionInitAs == null || !versionInitAs.equalsIgnoreCase(versionKey)) {
            return false;
        }
        return true;
    }

    public static int getInitializedVersion(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String versionInitAs = pref.getString("version_init", null);
        if (versionInitAs == null)
            return 0;
        else {
            return (int)Double.parseDouble(versionInitAs);
        }
    }

    public static void setHasBeenInitialized(Context context, int version) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        pref.edit().putString("version_init", String.valueOf(version)).commit();
    }

    public static void saveValue(Context context, String key, String value) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putString(key, value).commit();
    }

    public static String getValue(Context context, String key, String defaultValue) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(key, defaultValue);
    }

    public static boolean getValue(Context context, int keyStringResourceId, boolean defaultValue) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getBoolean(context.getString(keyStringResourceId), defaultValue);
    }

    public static boolean isAuthenticatedWithWarning(Context context) {
        if (!isAuthenticated()){
            Toast.makeText(context, "you need to login to do that",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }



}
