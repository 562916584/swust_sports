package com.example.liqingfeng.swust_sports;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private Map<String,String> map;
    private String json;
    public static  String image,code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //设置全屏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏

        setContentView(R.layout.activity_main);
        final View view = View.inflate(this, R.layout.activity_main, null);
        setContentView(view);
        //setContentView(R.layout.activity_main);
        //handler.sendEmptyMessageDelayed(0,3000);
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
        String url="http://wangzhengyu.cn/api/verify/getVerify.do";
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
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
                map=new HashMap<String, String>();
                Gson gson = new Gson();
                ResponseModel object = gson.fromJson(json,ResponseModel.class);
                map=(Map<String, String>) object.getData();
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        image=map.get("img");
                        code=map.get("code");
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
