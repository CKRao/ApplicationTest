package com.clark.volley;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CLARKRAO";
    private Button mStringRequest;
    private Button mJsonRequest;
    private Button mImageRequest;
    private Button mImageLoader;
    private Button mNetworkImageViewBt;
    private Button mXmlRequest;
    private Button mGsonRequest;
    private ImageView mImg;
    private NetworkImageView mNetworkImageView;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mQueue = Volley.newRequestQueue(this);
        initUI();
        initEvent();
    }

    private void initUI() {
        mStringRequest = (Button) findViewById(R.id.StringRequest);
        mJsonRequest = (Button) findViewById(R.id.JsonRequest);
        mImageRequest = (Button) findViewById(R.id.ImageRequest);
        mImageLoader = (Button) findViewById(R.id.ImageLoader);
        mNetworkImageViewBt = (Button) findViewById(R.id.NetworkImageViewBt);
        mNetworkImageView = (NetworkImageView) findViewById(R.id.NetworkImageView);
        mXmlRequest = (Button) findViewById(R.id.XmlRequest);
        mGsonRequest = (Button) findViewById(R.id.GsonRequest);
        mImg = (ImageView) findViewById(R.id.img);
    }

    private void initEvent() {
        mStringRequest.setOnClickListener(this);
        mJsonRequest.setOnClickListener(this);
        mImageRequest.setOnClickListener(this);
        mImageLoader.setOnClickListener(this);
        mNetworkImageViewBt.setOnClickListener(this);
        mXmlRequest.setOnClickListener(this);
        mGsonRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.StringRequest:
                stringRequestMethod();
                break;
            case R.id.JsonRequest:
                jsonRequestMethod();
                break;
            case R.id.ImageRequest:
                imageRequestMethod();
                break;
            case R.id.ImageLoader:
                imageLoaderMethod();
                break;
            case R.id.NetworkImageViewBt:
                networkImageView();
                break;
            case R.id.XmlRequest:
                xmlRequestMethod();
                break;
            case R.id.GsonRequest:
                gsonRequestMethod();
                break;

        }
    }

    private void gsonRequestMethod() {
        GsonRequest<WeatherInfo> gsonRequest = new GsonRequest<>(Request.Method.GET,
                "http://api.jisuapi.com/weather/query?appkey=b968e6d10975a8c5&city=安顺",
                WeatherInfo.class,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                    }
                },
                new Response.Listener<WeatherInfo>() {
                    @Override
                    public void onResponse(WeatherInfo response) {
                        WeatherInfo weatherInfo = response;
                        Log.d("TAG", "city is " + weatherInfo.getResult().getCity());
                        Log.d("TAG", "temp is " + weatherInfo.getResult().getTemp());
                        Log.d("TAG", "updatetime is " + weatherInfo.getResult().getUpdatetime());
                    }
                });
        mQueue.add(gsonRequest);
    }

    private void xmlRequestMethod() {
        XmlRequest xmlRequest = new XmlRequest(Request.Method.GET,
                "http://flash.weather.com.cn/wmaps/xml/china.xml",
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                    }
                },
                new Response.Listener<XmlPullParser>() {
                    @Override
                    public void onResponse(XmlPullParser response) {
                        try {
                            int eventType = response.getEventType();
                            while (eventType != XmlPullParser.END_DOCUMENT) {
                                switch (eventType) {
                                    case XmlPullParser.START_TAG:
                                        String nodeName = response.getName();
                                        if ("city".equals(nodeName)) {
                                            String pName = response.getAttributeValue(0);
                                            Log.d("TAG", "pName is" + pName);
                                        }
                                        break;
                                }
                                eventType = response.next();
                            }
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        mQueue.add(xmlRequest);
    }

    private void stringRequestMethod() {
        StringRequest stringRequest = new StringRequest("http://blog.csdn.net/guolin_blog/article/details/17482095",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, error.getMessage());
                    }
                });
        mQueue.add(stringRequest);
    }

    private void jsonRequestMethod() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("http://api.jisuapi.com/weather/query?" +
                "appkey=b968e6d10975a8c5&city=安顺",
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.getMessage());
            }
        });
        mQueue.add(jsonObjectRequest);
    }

    private void imageRequestMethod() {
        ImageRequest imageRequest = new ImageRequest("http://p0.qhmsg.com/t01ffbba1b6b578fa8b.jpg",
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        mImg.setImageBitmap(response);
                    }
                }, 0, 0, Bitmap.Config.ARGB_8888,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mImg.setImageResource(R.mipmap.ic_launcher);
                    }
                });
        mQueue.add(imageRequest);
    }

    private void imageLoaderMethod() {
//        ImageLoader imageLoader = new ImageLoader(mQueue, new ImageLoader.ImageCache() {
//            @Override
//            public Bitmap getBitmap(String url) {
//                return null;
//            }
//
//            @Override
//            public void putBitmap(String url, Bitmap bitmap) {
//
//            }
//        });
        ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());
        ImageLoader.ImageListener listener =
                ImageLoader.getImageListener(mImg, 0, R.mipmap.ic_launcher);
        imageLoader.get("http://c3.haibao.cn/img/600_0_100_0/" +
                "1462439534.3843/51e01b90ce25ef3f7fd2d15441e39625.jpg", listener);

    }

    private void networkImageView() {
        ImageLoader imageLoader1 = new ImageLoader(mQueue, new BitmapCache());
        mNetworkImageView.setDefaultImageResId(0);
        mNetworkImageView.setErrorImageResId(R.mipmap.ic_launcher);
        mNetworkImageView.setImageUrl("http://imgsrc.baidu.com/forum/pic/item/" +
                "738b4710b912c8fc11273462fc039245d788214d.jpg", imageLoader1);
    }
}
