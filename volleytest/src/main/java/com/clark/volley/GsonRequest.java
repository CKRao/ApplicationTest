package com.clark.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

/**
 * Created by clark on 2016/11/19.
 */

public class GsonRequest<T> extends Request<T> {
    private final Response.Listener<T> mListener;
    private Class<T> mClass;
    private Gson mGson;

    public GsonRequest(int method, String url, Class<T> clazz, Response.ErrorListener listener,
                       Response.Listener<T> listener1) {
        super(method, url, listener);
        mGson = new Gson();
        mListener = listener1;
        mClass = clazz;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String JSONString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(mGson.fromJson(JSONString, mClass),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }
}
