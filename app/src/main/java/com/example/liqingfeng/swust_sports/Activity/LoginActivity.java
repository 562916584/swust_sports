package com.example.liqingfeng.swust_sports.Activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liqingfeng.swust_sports.Tools.Configuration_BaseUrl;
import com.example.liqingfeng.swust_sports.View.CustomVideoView;
import com.example.liqingfeng.swust_sports.JellyInterpolator;
import com.example.liqingfeng.swust_sports.Tools.Json_analyze;
import com.example.liqingfeng.swust_sports.R;
import com.example.liqingfeng.swust_sports.ResponseModel;
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

public class LoginActivity extends Activity{
    private TextView mBthLogin;
    private View progress;
    private View mInputLayout;
    private float mWidth, mHeight;
    private LinearLayout mName, mpas,mver;
    private CustomVideoView videoview;
    private ImageView verify_imageview;
    private EditText username,password,verify;
    //登陆接口
    private String url= Configuration_BaseUrl.getLogin_interface(),url_login;
    //验证码字符串
    public String imag_String,verify_code;
    public String json;
    public Map<String,String> map;


    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //设置全屏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_login);


        //判断是否是第一次访问
        judge_start_access();
        initView();

    }

    //判断是否是第一次访问
    private void judge_start_access()
    {

        sharedPreferences = getSharedPreferences("count",MODE_PRIVATE);
        int count = sharedPreferences.getInt("count",0);
        Log.d("print", String.valueOf(count));
        //判断程序是第几次运行，如果是第一次运行则跳转到引导页面
        if (count == 0){
            Intent intent1=getIntent();
            imag_String=intent1.getStringExtra("img");
            verify_code=intent1.getStringExtra("code");
            Intent intent = new Intent();
            intent.putExtra("img",imag_String);
            intent.putExtra("code",verify_code);
            intent.setClass(getApplicationContext(), WelcomeActivity.class);
            startActivity(intent);
            this.finish();
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //存入数据
        editor.putInt("count",++count);
        //提交修改
        editor.commit();
    }

    //绘制验证码
    public void drawverify(){
        Bitmap bitmap= stringToBitmap(imag_String);
        verify_imageview=findViewById(R.id.verify_image);
        verify_imageview.setImageBitmap(bitmap);
    }
    //网络请求验证码
    public void requerst_verify(){
        //验证码接口
        String url=Configuration_BaseUrl.getVerify_interface();
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
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imag_String=map.get("img");
                        verify_code=map.get("code");
                    }
                });
            }
        });
    }
    //验证码图片点击事件
    public void redraw(View view)
    {
        requerst_verify();
        drawverify();
    }
    private void initView() {
        //获取验证码字符串,并且显示出来
        Intent intent=getIntent();
        imag_String=intent.getStringExtra("img");
        verify_code=intent.getStringExtra("code");
        drawverify();

        mBthLogin = (TextView) findViewById(R.id.main_btn_login);
        progress = findViewById(R.id.layout_progress);
        mInputLayout = findViewById(R.id.input_layout);
        mName = (LinearLayout) findViewById(R.id.input_layout_name);
        mpas = (LinearLayout) findViewById(R.id.input_layout_psw);
        mver=(LinearLayout)findViewById(R.id.input_ver);
        //三个Edittext输入框 获取输入输出
        username=(EditText) findViewById(R.id.input_username);
        password=(EditText) findViewById(R.id.input_password);
        verify=(EditText) findViewById(R.id.input_verify);

        videoview = (CustomVideoView) findViewById(R.id.vidoview);
        videoview.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.sport));

        //播放
        videoview.start();
        //循环播放
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoview.start();
            }
        });

        //焦点监听
        verify.setOnFocusChangeListener( new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    verify_imageview.setVisibility(View.VISIBLE);
                }
                else
                {
                    verify_imageview.setVisibility( View.INVISIBLE );
                }
            }
        } );

    }


    public void logining(View v) {

        if(username.getText().toString().trim().equals("")||password.getText().toString().trim().equals("")
                ||verify.getText().toString().trim().equals(""))
        {
            Toast.makeText(LoginActivity.this,"输入不能为空",Toast.LENGTH_SHORT).show();
        }
        else {
            url_login = requesturl();
            //get方式发送请求数据，实现登陆操作
            login_progress(url_login);

        }

        /*
        //和动画线程分离 实现登陆操作
        new Thread(){
            public void run(){
                try {
                    //耗时操作
                    sleep(2000);
                    Intent intent=new Intent(LoginActivity.this,MaininterfaceActivity.class);
                    onStop();
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }.start();
        */
    }

    //登陆动画开始
    private void animation_start()
    {

        // 计算出控件的高与宽
        mWidth = mBthLogin.getMeasuredWidth();
        mHeight = mBthLogin.getMeasuredHeight();
        // 隐藏输入框
        mName.setVisibility(View.INVISIBLE);
        mpas.setVisibility(View.INVISIBLE);
        mver.setVisibility(View.INVISIBLE);
        inputAnimator(mInputLayout, mWidth, mHeight);
    }

    //返回URL 登陆接口
    private String requesturl() {
        String usName,usPassword,verifyCode,code;
        usName=username.getText().toString();
        usPassword=password.getText().toString();
        verifyCode=verify_code;
        code=verify.getText().toString();
        //密码加密
        usPassword += "swust_sport";
        usPassword = android.util.Base64.encodeToString(usPassword.getBytes(),
                android.util.Base64.DEFAULT);
        url=url+"?"+"usName="+usName+"&usPassword="+usPassword+"&verifyCode="+verifyCode +"&code="+code;
        return url;
    }

    //登陆请求操作
    private void login_progress(String Url)
    {
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
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
                //如果请求失败，用日志记录
                Log.e("TAG", "失败"+getClass().toString()+"结果"+e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                //在异步操作中嵌UI线程，进行UI的更新
                final ResponseModel finalResponseModel = Json_analyze.getperson(response.body().string(),ResponseModel.class);
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String,Object> map=(Map<String,Object>)finalResponseModel.getData();

                        if((Double)map.get("status")==2.0)
                        {
                            Toast.makeText(LoginActivity.this,"用户不存在",Toast.LENGTH_SHORT).show();
                            //recover();
                        }
                        else if ((Double)map.get("status")==1.0)
                        {
                            //登陆动画
                            Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                            animation_start();
                        }
                        else if((Double)map.get("status")==3.0)
                        {
                            Toast.makeText(LoginActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
                            //recover();
                        }
                        else if((Double)map.get("status")==4.0)
                        {
                            Toast.makeText(LoginActivity.this,"验证码错误",Toast.LENGTH_SHORT).show();
                            //recover();
                        }
                        else if((Double)map.get("status")==5.0)
                        {
                            Toast.makeText(LoginActivity.this,"用户已被锁定",Toast.LENGTH_SHORT).show();
                            //recover();
                        }
                    }
                });
            }
        });
    }

    //输入框的动画效果
    private void inputAnimator(final View view, float w, float h) {
        AnimatorSet set=new AnimatorSet();
        final ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) animator.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });
        ObjectAnimator animator2=ObjectAnimator.ofFloat(mInputLayout,"scaleX",1f,0.5f);
        set.setDuration(500);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator,animator2);
        set.start();
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                //动画结束
                progress.setVisibility(view.VISIBLE);
                progressAnimator(progress);
                mInputLayout.setVisibility(View.INVISIBLE);
            }


            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);
        animator3.setDuration(1000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();
        animator3.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(LoginActivity.this,MaininterfaceActivity.class);
                onStop();
                startActivity(intent);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    //返回重启加载
    @Override
    protected void onRestart() {
        super.onRestart();
        initView();
    }

    //防止锁屏或者切出的时候，音乐在播放
    @Override
    protected void onStop() {
        super.onStop();
        videoview.stopPlayback();
    }

    //打开新Activity 得到返回值
    public void register(View view)
    {
        //得到返回值
        startActivityForResult(new Intent(LoginActivity.this,RegisterActivity.class),1);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        String result=data.getExtras().getString("result");
        EditText usname=findViewById(R.id.input_username);
        usname.setText(result);
    }


    //绘制验证码
    public Bitmap stringToBitmap(String string) {
        //将字符串转换成Bitmap类型
        Bitmap bitmap=null;
        try {
            byte[]bitmapArray;
            bitmapArray= Base64.decode(string, Base64.DEFAULT);
            bitmap= BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}

