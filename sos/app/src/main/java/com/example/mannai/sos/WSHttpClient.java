package com.example.mannai.sos;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.Base64;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

import android.content.Context;
import android.util.Log;


import com.loopj.android.http.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;
//import javax.net.ssl.SSLSocketFactory;

public class WSHttpClient {

    final int DEFAULT_TIMEOUT = 30 * 1000;

    private  static final String BASE_URL = "http://173.255.210.24/";

    public static AsyncHttpClient client;
    private Context context;
    private  MySSLSocketFactory sf;

    public WSHttpClient(Context c){
        this.context=c;
        client = new AsyncHttpClient();//true,80,443);

        //client.setSSLSocketFactory(getSSLSocketFactory());
        client.setTimeout(DEFAULT_TIMEOUT);
        //client.setURLEncodingEnabled(true);
        PersistentCookieStore myCookieStore = new PersistentCookieStore(context);

        client.setCookieStore(myCookieStore);
        //client.setSSLSocketFactory(setTrust("init"));

        //setCertificate("init");

    }





    public void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        Log.i("SSLx","setting done "+url);
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }




    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }





}
