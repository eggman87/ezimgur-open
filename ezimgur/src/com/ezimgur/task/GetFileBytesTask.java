package com.ezimgur.task;

import android.content.Context;
import com.ezimgur.instrumentation.Log;
import com.ezimgur.view.utils.GifDecoder;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * Copyright NCR Inc,
 * User: matthewharris
 * Date: 12/29/13
 * Time: 1:13 PM
 */
public class GetFileBytesTask extends LoadingTask<GifDecoder> {

    private String url;

    protected GetFileBytesTask(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    public GifDecoder call() throws Exception {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(new HttpGet(url));

        GifDecoder decoder = new GifDecoder();
        decoder.read(response.getEntity().getContent(), 0);

        return decoder;
    }
}
