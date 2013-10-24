package com.ezimgur.api.http;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.Socket;
import java.rmi.UnknownHostException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/14/12
 * Time: 11:08 PM
 */
public class EzSSLSocketFactory extends SSLSocketFactory {

    SSLContext sslContext = SSLContext.getInstance("TLS");

    public EzSSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super(truststore);

        TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sslContext.init(null, new TrustManager[]{tm}, null);
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }

    @Override
    public X509HostnameVerifier getHostnameVerifier() {
        X509HostnameVerifier verifier = new X509HostnameVerifier() {

            @Override
            public void verify(String string, SSLSocket ssls) throws IOException {
                //trusted clients - not verifying.
            }

            @Override
            public void verify(String string, X509Certificate xc) throws SSLException {
                //trusted clients - not verifying.
            }

            @Override
            public void verify(String string, String[] strings, String[] strings1) throws SSLException {
                //trusted clients - not verifying.
            }

            @Override
            public boolean verify(String string, SSLSession ssls) {
                //trusted clients - not verifying.
                return true;
            }
        };
        return  verifier;
    }
}
