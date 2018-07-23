package com.example.liqingfeng.swust_sports;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
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

import java.io.IOException;
import java.util.Map;
import java.util.StringTokenizer;
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
    private String url="http://wangzhengyu.cn/api/user/login.do",url_login;
    //验证码字符串
    private String imag_String,verify_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //设置全屏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_login);

        initView();

    }

    private void initView() {
        //获取验证码字符串,并且显示出来
        Intent intent=getIntent();
        imag_String=intent.getStringExtra("img");
        verify_code=intent.getStringExtra("code");
        Bitmap bitmap= stringToBitmap(imag_String);
        verify_imageview=findViewById(R.id.verify_image);
        verify_imageview.setImageBitmap(bitmap);

        mBthLogin = (TextView) findViewById(R.id.main_btn_login);
        progress = findViewById(R.id.layout_progress);
        mInputLayout = findViewById(R.id.input_layout);
        mName = (LinearLayout) findViewById(R.id.input_layout_name);
        mpas = (LinearLayout) findViewById(R.id.input_layout_psw);
        mver=(LinearLayout)findViewById(R.id.input_ver);
        //三个Edittext输入框 获取输入输出
        username=findViewById(R.id.input_username);
        password=findViewById(R.id.input_password);
        verify=findViewById(R.id.input_verify);

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


    }

    public void verify(View view) {

        verify_imageview.setVisibility(view.VISIBLE);

    }
    //登陆失败 恢复之前的样子
    public void recover()
    {
        finish();
        startActivity(getIntent());

    }

    public void logining(View v) {

        url_login=requesturl();
        //get方式发送请求数据，实现登陆操作
        login_progress(url_login);

        // 计算出控件的高与宽
        mWidth = mBthLogin.getMeasuredWidth();
        mHeight = mBthLogin.getMeasuredHeight();
        // 隐藏输入框
        mName.setVisibility(View.INVISIBLE);
        mpas.setVisibility(View.INVISIBLE);
        mver.setVisibility(View.INVISIBLE);
        inputAnimator(mInputLayout, mWidth, mHeight);

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
                            recover();
                        }
                        else if ((Double)map.get("status")==1.0)
                        {
                            Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(LoginActivity.this,MaininterfaceActivity.class);
                            onStop();
                            startActivity(intent);
                        }
                        else if((Double)map.get("status")==3.0)
                        {
                            Toast.makeText(LoginActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
                            recover();
                        }
                        else if((Double)map.get("status")==4.0)
                        {
                            Toast.makeText(LoginActivity.this,"验证码错误",Toast.LENGTH_SHORT).show();
                            recover();
                        }
                        else if((Double)map.get("status")==5.0)
                        {
                            Toast.makeText(LoginActivity.this,"用户已被锁定",Toast.LENGTH_SHORT).show();
                            recover();
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
        set.setDuration(1000);
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


    //自动收起输入法
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                boolean res=hideKeyboard(v.getWindowToken());
                if(res){
                    //隐藏了输入法，则不再分发事件
                    return true;
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private boolean hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            return  im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
        return false;
    }

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

