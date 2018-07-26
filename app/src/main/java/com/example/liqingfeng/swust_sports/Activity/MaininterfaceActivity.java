package com.example.liqingfeng.swust_sports.Activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.liqingfeng.swust_sports.R;
import com.example.liqingfeng.swust_sports.Tools.ColorShades;
import com.example.liqingfeng.swust_sports.View.CircleImageView;
import com.example.liqingfeng.swust_sports.View.FragmentTabHost;

import java.util.Map;


public class MaininterfaceActivity extends Activity {

    public static final String TAG = "MaininterfaceActivity";
    private DrawerLayout mDrawe;
    private FragmentTabHost mTobHost;
    private FrameLayout mTabContent;
    private View mTabView;
    private String[] mTabTexts;

    private int [] mTabIcons=new int[]{
            R.drawable.selector_nvg_message,
            R.drawable.selector_nvg_contacts,
            R.drawable.selector_nvg_star
    };
    private TextView mTvTitle;
    private ImageView mIvAdd;
    private TextView mTvAdd;
    private TextView mTvMore;
    private RelativeLayout mRlTitle;
    private RelativeLayout mRlMenu;
    private ColorShades mColorShades;
    private LinearLayout mLlContentMain;
    private CircleImageView mCivHead;
    private TextView mTvMessgeCount;
    private TextView mTvContactsCount;
    private TextView mTvStarCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //设置全屏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏


        setContentView(R.layout.activity_maininterface);
    }

}
