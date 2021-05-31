package com.meteo;

import com.loopj.android.http.*;

public class Reseau {
    private static final String URLloc = "http://192.168.0.16"; //ip locale de l'arduino

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get( RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(URLloc, params, responseHandler);
    }

    public static void post( RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(URLloc, params, responseHandler);
    }


}