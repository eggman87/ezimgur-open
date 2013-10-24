package com.ezimgur.task;

import android.content.Context;
import com.ezimgur.api.ImageApi;
import com.ezimgur.datacontract.Image;
import com.google.inject.Inject;

/**
 * EggmanProjects
 * Author: Matthew Harris
 * Date: 10/2/12
 * Time: 8:41 PM
 */
public class GetImageTask extends LoadingTask<Image> {

    @Inject private ImageApi mImageApi;
    private String mImageHash;

    public GetImageTask(Context context, String imageHash) {
        super(context);
        mImageHash = imageHash;
    }

    @Override
    public Image call() throws Exception {
        return mImageApi.getImageById(mImageHash);
    }
}
