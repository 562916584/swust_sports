package com.example.liqingfeng.swust_sports.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.liqingfeng.swust_sports.Tools.Json_analyze;
import com.example.liqingfeng.swust_sports.R;
import com.example.liqingfeng.swust_sports.ResponseModel;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterActivity extends Activity {
    private EditText username;
    private EditText account;
    private EditText password1;
    private EditText password2;
    public static String acccountString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //设置全屏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏

        setContentView(R.layout.activity_register);

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                boolean res = hideKeyboard(v.getWindowToken());
                if (res) {
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
     *
     * @param token
     */
    private boolean hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            return im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
        return false;
    }

    public void registerbutton(View view) {
        String usernameString;
        String password1String;
        String password2String;
        String url="http://wangzhengyu.cn/sport/user/register.do";
        username = findViewById(R.id.uname);
        account = findViewById(R.id.account);
        password1 = findViewById(R.id.password1);
        password2=findViewById(R.id.password2);
        usernameString = username.getText().toString();
        acccountString = account.getText().toString();
        password1String = password1.getText().toString();
        password2String=password2.getText().toString();

        //判断不为空
        if (username.getText().toString().trim().equals("") || account.getText().toString().trim().equals("")
                || password1.getText().toString().trim().equals("")||password2.getText().toString().trim().equals("")) {
            /*//生成一个对话框，警告输入数据不能为空
            new AlertDialog.Builder(RegisterActivity.this)
                    .setTitle("警告").setMessage("输入不能为空，请重新输入！！！")
                    .setPositiveButton("确定", null).show();
                    */
            Toast.makeText(RegisterActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
        }
        if (!password1String.equals(password2String)){
            Toast.makeText(RegisterActivity.this,"再次输入密码不正确",Toast.LENGTH_SHORT).show();
        }

            //数据加密
            password1String += "swust_sport";
            password1String = android.util.Base64.encodeToString(password1String.getBytes(),
                    android.util.Base64.DEFAULT);
            sendform(usernameString, acccountString, password1String, url);

    }

    private void sendform(String usNickname, String usName, String usPassword,String url) {

        OkHttpClient okHttpClient  = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        //post提交表单数据
        FormBody formBody=new FormBody.Builder()
                .add("usName",usName)
                .add("usNickname",usNickname)
                .add("usPassword",usPassword)
                .build();
        final Request request=new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        //创建CALL 异步请求
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //如果请求失败，用日志记录
                Log.e("TAG", "失败"+getClass().toString()+"结果"+e.toString());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                //请求成功
                //Log.e("TAG","成功"+response.body().string());

                //在异步操作中嵌UI线程，进行UI的更新
                final ResponseModel finalResponseModel = Json_analyze.getperson(response.body().string(),ResponseModel.class);
                RegisterActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if( (Double)finalResponseModel.getData()==0.0)
                        {
                            Toast.makeText(RegisterActivity.this,"注册用户已经存在",Toast.LENGTH_SHORT).show();
                        }
                        else if ((Double)finalResponseModel.getData()==1.0)
                        {
                            Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                    }
                });
            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra("result",acccountString);
        this.setResult(1, intent);
        this.finish();
    }

    public void backTologin(View view)
    {
        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}

