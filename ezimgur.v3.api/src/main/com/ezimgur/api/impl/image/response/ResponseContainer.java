package com.ezimgur.api.impl.image.response;

import com.ezimgur.datacontract.Basic;
import com.ezimgur.datacontract.Image;
import com.ezimgur.datacontract.Upload;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/27/12
 * Time: 10:14 PM
 */
public class ResponseContainer {

    public class UploadImageResponse extends Basic<Upload> {


    }

    public class GetImageResponse extends Basic<Image> {

    }
}
