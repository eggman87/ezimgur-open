package com.ezimgur.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;
import com.ezimgur.api.ImageApi;
import com.ezimgur.api.http.BigMultiPartEntity;
import com.ezimgur.api.impl.image.request.payload.UploadImagePayload;
import com.ezimgur.datacontract.Upload;
import com.google.inject.Inject;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/7/12
 * Time: 10:41 PM
 */

public class UploadImageTask extends LoadingTask<Upload> {

    @Inject
    private ImageApi mImageApi;
    private ProgressDialog mProgressDialog;
    private UploadImagePayload mPayload;
    private int mProgress;


    private BigMultiPartEntity.ProgressListener listener = new BigMultiPartEntity.ProgressListener() {
        @Override
        public void transferred(int num) {
            mProgressDialog.setProgress(num);
        }
    };

    public UploadImageTask(Context context, String title, String caption, String imageUri) {
        super(context);
        mPayload = new UploadImagePayload(listener, imageUri);
        mPayload.setTitle(title);
        mPayload.setDescription(caption);

    }

    @Override
    public Upload call() throws Exception {
        return mImageApi.uploadImage(mPayload);
    }

    @Override
    protected void onSuccess(Upload imageUpload) throws Exception {
        super.onSuccess(imageUpload);
        Toast.makeText(getContext(), "uploaded image successfully - new ID = " + imageUpload.id, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onException(Exception e) throws RuntimeException {
        super.onException(e);

        Toast.makeText(getContext(), "Something went wrong, cause: "+e.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPreExecute() throws Exception {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMessage("Uploading image...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        mProgressDialog.setProgress(mProgress);
    }

    @Override
    protected void onFinally() throws RuntimeException {
        super.onFinally();
        mProgressDialog.dismiss();
    }
}
