package com.example.liqingfeng.swust_sports;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MaininterfaceActivity extends Activity {

    private TextView showtext;
    private  String json;
    private Map<String,String> map;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //设置全屏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏


        setContentView(R.layout.activity_maininterface);
    }
    /*
    public void textbutton(View view){
        //验证码接口
        String url="http://wangzhengyu.cn/api/verify/getVerify.do";
        //String url="http://wangzhengyu.cn/api/sports/sports.do";
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

                final String image;
                //单个解析json
                json=response.body().string();
                map=new HashMap<String, String>();
                Gson gson = new Gson();
                ResponseModel object = gson.fromJson(json,ResponseModel.class);
                map=(Map<String, String>) object.getData();
                image=map.get("img");
                MaininterfaceActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showtext=findViewById(R.id.show);
                        imageView=findViewById(R.id.yanzhengma);

                        Bitmap bitmap=stringToBitmap(image);
                        imageView.setImageBitmap(bitmap);
                    }
                });

                //多个List《Map》解析json
                json=response.body().string();
                Gson gson=new Gson();
                ResponseModel object=Json_analyze.getperson(json,ResponseModel.class);
                List<Map<String,String>> list=(List<Map<String,String>>)object.getData();
                System.out.println(list);

            }
        });

    }
    public Bitmap stringToBitmap(String string) {
        //将字符串转换成Bitmap类型
        Bitmap bitmap=null;
        try {
            byte[]bitmapArray;
            bitmapArray=Base64.decode(string, Base64.DEFAULT);
            bitmap=BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

*/
}
