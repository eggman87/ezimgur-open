package com.ezimgur.api.impl.album.response;

import com.ezimgur.datacontract.Album;
import com.ezimgur.datacontract.Basic;
import com.ezimgur.datacontract.Image;

import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/27/12
 * Time: 9:24 PM
 */
public class ResponseContainer {

    public class GetAlbumImagesContainer extends Basic <List<Image>> {

    }

    public class GetAlbumContainer extends Basic<Album> {

    }
}
