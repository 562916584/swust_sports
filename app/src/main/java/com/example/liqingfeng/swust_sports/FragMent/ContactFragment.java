package com.example.liqingfeng.swust_sports.FragMent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.liqingfeng.swust_sports.Activity.MaininterfaceActivity;
import com.example.liqingfeng.swust_sports.R;

public class ContactFragment extends Fragment{
    private String mTagtext;
    private static final String TAG = "ContactsFragment";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle arguments=getArguments();
        mTagtext= (String) arguments.get(MaininterfaceActivity.TAG);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");
        //绑定Fragment的布局文件，然后将xml文件转换成View对象返回
        View inflate = inflater.inflate(R.layout.fragment_contacts, null);
        TextView tvText = (TextView) inflate.findViewById(R.id.tv_text);
        if (mTagtext != null && !TextUtils.isEmpty(mTagtext)) {
            tvText.setText(mTagtext);
        } else {
            Log.i(TAG, "onCreateView: mTagText -- " + mTagtext);
            tvText.setText("Null");
        }

        return inflate;
    }

}
