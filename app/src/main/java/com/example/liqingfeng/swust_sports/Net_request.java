package com.example.liqingfeng.swust_sports;

import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Net_request {

    public  String json_String;//json数据

    public Net_request()
    {
    }

    public void swap(String json)
    {
        this.json_String=json;
    }

    public String getJson_String()
    {
        return this.json_String;
    }

    public void net1(String url)
    {
        //创建网络处理的对象
        OkHttpClient client= new OkHttpClient.Builder()
                //设置读取数据的时间
                .readTimeout(5, TimeUnit.SECONDS)
                //创建对象
                .build();
        //创建一个网络请求对象，如果没有写请求方式，默认是get
        //在请求对象里放入URL
        Request request=new Request.Builder()
                .get()
                .url(url)
                .build();
        //ca;;是用来执行请求的请求类
        Call call=client.newCall(request);
        //异步方法来执行任务处理，网络请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //如果请求失败，用日志记录
                Log.e("TAG", "失败"+getClass().toString()+"结果"+e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //成功 子线程发送请求  json数据直接用string()转 不要用tostring
                //response.body()多次引用会导致程序崩溃
                //Log.e("TAG","成功"+Thread.currentThread().getName()+"结果"+response.body().string());


                //List<users> users=new ArrayList<users>();
                //users=Json_analyze.getPersons1(response.body().string(),users.class);
                //Log.d("TAG",users.get(1).getName());
                //json_String=response.body().string();
                swap(response.body().string());
                Intent intent=new Intent("android.intent.action.MY_BROADCAST");
                intent.putExtra("msg","这是一个测试广播");
                //sendBroadcast(intent);
            }
        });
        //call.cancel(); 取消任务
        /*
        try{
            Response execute=call.execute();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
        */

    }


    public void net2(String url)
    {
        OkHttpClient client=new OkHttpClient.Builder()
                .readTimeout(6, TimeUnit.SECONDS)
                .build();
        //post请求数据创建一个RequestBody,存放重要数据的key和value
        RequestBody body=new FormBody.Builder()
                .add("rows","3")
                .add("page","1")
                .build();
        //创建一个请求对象,传入URL地址和相关数据键值对的对象
        Request request=new Request.Builder()
                .url("http://acm.swust.edu.cn/api/user.do")
                .post(body).build();
        //创建一个处理请求数据的操作类
        Call call=client.newCall(request);

        //使用异步操作请求数据
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG","错误信息"+e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                //Log.e("TAGO",response.body().string());
                json_String=response.body().string();
            }
        });
    }
}
