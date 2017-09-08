package com.bignewsmaker.makebignews.FunctionActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bignewsmaker.makebignews.ConstData;
import com.bignewsmaker.makebignews.R;
import com.bignewsmaker.makebignews.Speaker;


/**
 * Created by liminyan on 06/09/2017.
 * 搜索界面
 */

public class SearchActivity  extends AppCompatActivity {

    private ConstData const_data = ConstData.getInstance();// 设置访问全局变量接口
    private Speaker speaker = Speaker.getInstance();// 设置语音系统接口

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);



        //设置输入监控
        //设置更新函数

    }
}