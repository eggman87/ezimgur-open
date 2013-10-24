package com.ezimgur.task;

import android.content.Context;
import com.ezimgur.api.GalleryApi;
import com.ezimgur.datacontract.GalleryItem;
import com.ezimgur.datacontract.GallerySort;
import com.google.inject.Inject;

import java.util.List;

/**
 * EggmanProjects
 * Author: Matthew Harris
 * Date: 9/30/12
 * Time: 1:08 PM
 */
public class GetGalleryTask extends LoadingTask<List<GalleryItem>>{

    @Inject private GalleryApi mGalleryApi;
    private String mGalleryName;
    private GallerySort mSort;
    private int mPageNumber;

    public GetGalleryTask(Context context, String galleryName, int pageNumber, GallerySort sort) {
        super(context);
        mGalleryName = galleryName;
        mPageNumber = pageNumber;
        mSort = sort;
    }

    @Override
    public List<GalleryItem> call() throws Exception {
        return mGalleryApi.getGalleryItems(mGalleryName, mSort, mPageNumber);
    }


}
