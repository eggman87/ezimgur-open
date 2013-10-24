package com.ezimgur.api.impl.image.request.payload;

import com.ezimgur.api.http.BigMultiPartEntity;
import com.ezimgur.instrumentation.Log;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/16/12
 * Time: 8:51 PM
 */
public class UploadImagePayload extends BigMultiPartEntity {

    private static final String TAG = "EzImgur.API.UploadImagePayload";

    public UploadImagePayload(ProgressListener listener, String imageFileUrl) {
        super(HttpMultipartMode.STRICT, listener);

        this.addPart("image", new FileBody(new File(imageFileUrl)));
        this.setStringPart("type", "file");
    }

    public void setAlbumId(String albumId) {
        this.setStringPart("album_id", albumId);
    }

    public void setName(String name) {
        this.setStringPart("name", name);
    }

    public void setTitle(String title) {
        this.setStringPart("title", title);
    }

    public void setDescription(String description) {
        this.setStringPart("description", description);
    }

    private void setStringPart(String name, String value) {
        try {
            addPart(name, new StringBody(value));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e(TAG, "unable to set string property on image upload");
        }
    }
}
