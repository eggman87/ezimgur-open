package com.ezimgur.api.http;

import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.exception.ApiException;
import com.ezimgur.datacontract.AuthenticationToken;
import com.ezimgur.datacontract.Basic;
import com.ezimgur.instrumentation.Log;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/14/12
 * Time: 10:12 PM
 */
public class HttpConnection {

    private static final int CONNECTION_TIMEOUT = 10000;
    private static final int SOCKET_TIMEOUT = 10000;
    private static AuthenticationToken authenticationToken;
    private static HttpClient sSharedHttpClient;

    private static final String TAG = "EzImgur.HttpConnection";

    public static void setAuthenticationToken(AuthenticationToken token) {
        if (token == null)
            authenticationToken = null;
        else {
            authenticationToken = new AuthenticationToken();
            authenticationToken.accessToken = token.accessToken;
            authenticationToken.expires = token.expires;
            authenticationToken.accountUserName = token.accountUserName;
            authenticationToken.refreshToken = token.refreshToken;
            authenticationToken.tokenType = token.tokenType;
        }
    }

    public void sendGetRequest(ApiRequest request) throws ApiException {
        String requestUrl = request.getRequestUrl();
        HttpGet get = new HttpGet(requestUrl);
        sendRequest(request, get);
    }



    public <TReq extends BigMultiPartEntity, TRes extends Basic> void sendMultiPartPostRequest(ApiMultiPartPostRequest<TReq, TRes> request) throws ApiException {
        HttpPost post = new HttpPost(request.getRequestUrl());
        post.setEntity(request.itemToSend);

        sendRequest(request, post);
    }

    public void sendUrlEncodedPostRequest(ApiUrlEncodedPostRequest request) throws ApiException{
        HttpPost post = new HttpPost(request.getRequestUrl());
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(request.getUrlEncodedEntityValues());
            entity.setContentType("application/x-www-form-urlencoded");
            post.addHeader("Content-Type","application/x-www-form-urlencoded");
            post.setEntity(entity);

            Log.d(TAG, "posting url encoded entity "+ EntityUtils.toString(entity));
            sendRequest(request, post);


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new ApiException("failed to build request entity ");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void sendDeleteRequest(ApiRequest request) throws ApiException {
        HttpDelete delete = new HttpDelete(request.getRequestUrl());
        sendRequest(request, delete);
    }

    public<TReq, TRes extends Basic> void sendPostRequest(ApiRequest<TReq, TRes> request) throws ApiException {
        HttpPost post = new HttpPost(request.getRequestUrl());
        String json = request.serializeRequest();
        if (Log.LOG_ENABLED)
            Log.d(TAG, "Setting HTTP POST request JSON: \n"+json);
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, HTTP.UTF_8);
            entity.setContentType("application/json");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        post.setEntity(entity);
        sendRequest(request, post);
    }

    private<TReq, TRes extends Basic> void sendRequest(ApiRequest<TReq, TRes> request, HttpUriRequest httpRequest )
            throws ApiException {
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParameters, SOCKET_TIMEOUT);

        httpRequest.setHeader(ImgurApiConstants.HEADER_CLIENT_AUTH, ImgurApiConstants.HEADER_CLIENT_AUTH_PREFIX + ImgurApiConstants.CLIENT_ID);
        if (authenticationToken != null )
            httpRequest.setHeader(ImgurApiConstants.HEADER_CLIENT_AUTH, ImgurApiConstants.HEADER_CLIENT_USER_AUTH_PREFIX + authenticationToken.accessToken);

        HttpClient client = getNewHttpClient(httpParameters);
        HttpResponse response = null;

        try {
            logRequestInfo(request, httpRequest);

            response = client.execute(httpRequest);

            String responseData = EntityUtils.toString(response.getEntity());
            if (Log.LOG_ENABLED)
                Log.d(TAG,"Response JSON: " + responseData);

            if (response.getStatusLine().getStatusCode() < 400) {
                request.deserializeResponseString(responseData);
            } else {
                checkRequestLimitHeaders(response);
                int responseCode = (response == null || response.getStatusLine() == null) ? 501:response.getStatusLine().getStatusCode();
                request.deserializeResponseError(responseData, responseCode);
            }

        } catch (Exception e) {
            String message = e.getMessage() == null ? "Unknown network error":e.getMessage();
            throw new ApiException(message, e);
        } finally {
            //need to make sure we dont need to free any resources here...
        }
    }

    private HttpClient getNewHttpClient(HttpParams params) {
        if (sSharedHttpClient == null){
            try {
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);

                SSLSocketFactory sf = new EzSSLSocketFactory(trustStore);
                sf.setHostnameVerifier(sf.getHostnameVerifier());

                HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
                HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

                SchemeRegistry registry = new SchemeRegistry();
                registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                registry.register(new Scheme("https", sf, 443));

                ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

                sSharedHttpClient = new DefaultHttpClient(ccm, params);
            } catch (Exception e) {
                sSharedHttpClient = new DefaultHttpClient();
            }
        }
        return sSharedHttpClient;
    }

    private void checkRequestLimitHeaders(HttpResponse response) throws ApiException {
        int remainingUserRequestCount = 0;
        Header userRemainingHeader = response.getFirstHeader("X-RateLimit-UserRemaining");
        if (userRemainingHeader != null) {
            remainingUserRequestCount = Integer.valueOf(userRemainingHeader.getValue());
            if (remainingUserRequestCount <= 0) {
                long timeResetInMs = 0;
                Header timeResetHeader = response.getFirstHeader("X-RateLimit-UserReset");
                if (timeResetHeader != null)
                {
                    timeResetInMs = Long.valueOf(timeResetHeader.getValue());
                }

                throw new ApiException(String.format("you have reached the maximum number of requests to imgur " +
                        "and must wait %s seconds. or you can signout", timeResetInMs/1000));
            }
        }
    }

    private void logRequestInfo(ApiRequest request, HttpUriRequest httpRequest) {
        if (Log.LOG_ENABLED) {

            Log.d(TAG, "Making HTTP "+ request.getRequestMethod() +" request to "+request.getRequestUrl());
            Header[] headers = httpRequest.getAllHeaders();
            for (Header head : headers) {
                Log.d(TAG, String.format("with header: %s=%s ",head.getName(), head.getValue()));
            }
        }
    }
}
