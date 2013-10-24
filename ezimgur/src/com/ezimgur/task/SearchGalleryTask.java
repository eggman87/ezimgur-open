package com.ezimgur.task;

import android.content.Context;
import com.ezimgur.api.GalleryApi;
import com.ezimgur.datacontract.GalleryItem;
import com.google.inject.Inject;

import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/24/12
 * Time: 5:31 PM
 */
public class SearchGalleryTask extends LoadingTask<List<GalleryItem>> {

    @Inject GalleryApi mGalleryApi;
    private String mSearchTerms;

    protected SearchGalleryTask(Context context, String searchTerms) {
        super(context);
        mSearchTerms = searchTerms;
    }

    @Override
    public List<GalleryItem> call() throws Exception {
        return mGalleryApi.searchGallery(mSearchTerms);
    }
}
