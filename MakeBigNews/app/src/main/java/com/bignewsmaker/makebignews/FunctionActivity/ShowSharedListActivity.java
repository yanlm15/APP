package com.bignewsmaker.makebignews.FunctionActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bignewsmaker.makebignews.Functiontool.ConstData;
import com.bignewsmaker.makebignews.R;
import com.bignewsmaker.makebignews.Functiontool.Speaker;

/**
 * Created by liminyan on 06/09/2017.
 * 用于显示收藏的新闻界面
 * 我们看情况添加"删除"操作
 * 显示标题
 * 图片（根据const_data确定是否显示）
 * 正文前50个字
 */

public class ShowSharedListActivity extends AppCompatActivity {

    private ConstData const_data = ConstData.getInstance();// 设置访问全局变量接口
    private Speaker speaker = Speaker.getInstance();// 设置语音系统接口

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //设置输入监控
        //设置更新函数

    }
}