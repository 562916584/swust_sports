package com.example.liqingfeng.swust_sports.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.example.liqingfeng.swust_sports.R;
import com.example.liqingfeng.swust_sports.ResponseModel;
import com.example.liqingfeng.swust_sports.Tools.Configuration_BaseUrl;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private Map<String,String> map;
    private String json;
    public static  String image,code;

    public static final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final View view = View.inflate(this, R.layout.activity_main, null);
        setContentView(view);

        AlphaAnimation aa = new AlphaAnimation(0.1f,1.0f);
        aa.setDuration(3000);
        view.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationEnd(Animation arg0) {
                getHome(image,code);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationStart(Animation animation) {
                request_image();
            }

        });
    }

    public void request_image()
    {
        //验证码接口
        //String url="http://wangzhengyu.cn/api/verify/getVerify.do";
        String url=Configuration_BaseUrl.getVerify_interface();
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .cookieJar( new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.put( url.host(), cookies );
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get( url.host() );
                        return cookies != null ? cookies : new ArrayList<Cookie>(  );
                    }
                } )
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        final Request request=new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call=okHttpClient.newCall(request);
        //异步亲求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG","失败"+e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                //单个解析json
                json=response.body().string();
                Gson gson = new Gson();
                ResponseModel object = gson.fromJson(json,ResponseModel.class);
                final String img=(String) object.getData();
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        image=img;
                    }
                });
            }
        });
    }
    public void getHome(String image,String code){
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        intent.putExtra("img",image);
        intent.putExtra("code",code);
        startActivity(intent);
        finish();
    }
}
