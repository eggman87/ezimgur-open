package com.ezimgur.data;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/29/12
 * Time: 6:22 PM
 */
public class AboutItem {

    private String mTitle;
    private String mBody;

    public AboutItem(String title, String body) {
        mTitle = title;
        mBody = body;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getBody() {
        return mBody;
    }
}
