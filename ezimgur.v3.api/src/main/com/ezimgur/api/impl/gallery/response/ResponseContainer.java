package com.ezimgur.api.impl.gallery.response;

import com.ezimgur.datacontract.*;

import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/15/12
 * Time: 3:22 PM
 */
public class ResponseContainer {

    public class GalleryLoadContainer extends Basic<List<GalleryItem>> {
    }

    public class GalleryImageContainer extends Basic<GalleryImage> {
    }

    public class GalleryAlbumContainer extends Basic<GalleryAlbum> {
    }

    public class GalleryItemContainer extends Basic<GalleryItem> {
    }

    public class VoteResponse extends Basic<Vote> {

    }

    public class BasicBooleanResponse extends Basic<Boolean> {

    }

    public class GalleryItemCommentsContainer extends Basic<List<Comment>> {

    }
}
