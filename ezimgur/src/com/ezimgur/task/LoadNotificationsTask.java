package com.ezimgur.task;

import android.content.Context;
import com.ezimgur.api.AccountApi;
import com.ezimgur.datacontract.Notification;
import com.google.inject.Inject;

import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/28/12
 * Time: 9:20 PM
 */
public class LoadNotificationsTask extends LoadingTask<List<Notification>> {

    @Inject
    private AccountApi mAccountApi;

    protected LoadNotificationsTask(Context context) {
        super(context);
    }

    @Override
    public List<Notification> call() throws Exception {

        return mAccountApi.getAccountNotifications();
    }
}
