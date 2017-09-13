package com.bignewsmaker.makebignews.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.bignewsmaker.makebignews.R;
import com.bignewsmaker.makebignews.basic_class.ConstData;

import java.util.Set;

public class AboutActivity extends AppCompatActivity {
    private ConstData const_data = ConstData.getInstance();// 设置访问全局变量接口
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView textView=(TextView)findViewById(R.id.about);
        Set<String> hs= const_data.getHaveRead();
        String s="";
        for(String e:hs)
            s+=e+"  ";
        textView.setText(s);
    }
}
