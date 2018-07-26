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

public class StartFragment extends Fragment{
    private static final String TAG = "StarFragment";
    private String mTagtext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mTagtext = arguments.getString(MaininterfaceActivity.TAG);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");
        View inflate = inflater.inflate(R.layout.fragment_star, null);
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
