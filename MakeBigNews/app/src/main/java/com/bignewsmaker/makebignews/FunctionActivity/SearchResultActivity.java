package com.bignewsmaker.makebignews.FunctionActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bignewsmaker.makebignews.Functiontool.ConstData;
import com.bignewsmaker.makebignews.R;
import com.bignewsmaker.makebignews.Functiontool.Speaker;

/**
 * Created by liminyan on 06/09/2017.
 * 这个界面我们讨论一下再决定要不要
 * 传入的搜索信息储存在const_data.getSearch_message();
 * 传入的分类（标签）信息储存在const_data.getSearch_class();
 */

public class SearchResultActivity extends AppCompatActivity {

    private ConstData const_data = ConstData.getInstance();// 设置访问全局变量接口
    private Speaker speaker = Speaker.getInstance();// 设置语音系统接口

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_search_result);

        //设置输入监控
        //设置更新函数

    }
}
