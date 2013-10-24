package com.ezimgur.api.impl.gallery.request;

import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.http.ApiGetRequest;
import com.ezimgur.api.impl.gallery.response.ResponseContainer;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/15/12
 * Time: 1:33 PM
 */
public class SearchGalleryRequest extends ApiGetRequest<ResponseContainer.GalleryLoadContainer> {

    private String searchTerms;

    public SearchGalleryRequest(String searchTerms) {
        this.searchTerms = searchTerms;
    }

    @Override
    public String getRequestUrl() {
        try {
            return String.format(ImgurApiConstants.URL_GALLERY_SEARCH, URLEncoder.encode(searchTerms, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
           //this should never happen, but if it does return string without encoding
            return String.format(ImgurApiConstants.URL_GALLERY_SEARCH, searchTerms);
        }
    }

    @Override
    protected Class<ResponseContainer.GalleryLoadContainer> getResponseClass() {
        return ResponseContainer.GalleryLoadContainer.class;
    }
}
