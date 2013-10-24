package com.ezimgur.task;

import android.content.Context;
import com.ezimgur.api.exception.ApiException;
import com.ezimgur.app.EzApplication;
import com.ezimgur.view.event.OnTaskLoadEvent;
import com.google.inject.Inject;
import roboguice.event.EventManager;
import roboguice.util.RoboAsyncTask;

/**
 * Created with IntelliJ IDEA.
 * User: Matt
 * Date: 10/22/12
 * Time: 8:29 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class  LoadingTask <TResult>  extends RoboAsyncTask<TResult> {

    @Inject
    protected EventManager mEventManager;

    protected boolean mUseProgress = true;

    protected LoadingTask(Context context) {
        super(context);
    }

    @Override
    protected void onPreExecute() throws Exception {
        super.onPreExecute();
        if (mUseProgress)
            mEventManager.fire(new OnTaskLoadEvent(OnTaskLoadEvent.TaskLoading.LOAD_STARTED));
    }

    @Override
    protected void onException(Exception e) throws RuntimeException {
        super.onException(e);

        //if unauthorized - try to refresh token if we have one.
        if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;
            if (apiException.getStatusCode() == 401 || apiException.getStatusCode() == 403) {
                if (EzApplication.hasToken()) {
                    new AuthenticateUserTask(getContext()).execute();
                }
            }
        }
    }

    @Override
    protected void onFinally() throws RuntimeException {
        super.onFinally();
        if (mUseProgress)
            mEventManager.fire(new OnTaskLoadEvent(OnTaskLoadEvent.TaskLoading.LOAD_FINISHED));
    }
}
