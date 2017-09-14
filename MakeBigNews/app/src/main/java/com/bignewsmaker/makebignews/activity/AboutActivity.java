package com.bignewsmaker.makebignews.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.bignewsmaker.makebignews.R;
import com.bignewsmaker.makebignews.basic_class.ConstData;

public class AboutActivity extends AppCompatActivity {
    private ConstData const_data = ConstData.getInstance();// 设置访问全局变量接口

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView textView = (TextView) findViewById(R.id.about);
        String s = "Big News Maker团队：\n闫力敏（队长）\n张斐然\n陈星谷";
        textView.setText(s);
        textView.setTextSize(30);
    }
}
