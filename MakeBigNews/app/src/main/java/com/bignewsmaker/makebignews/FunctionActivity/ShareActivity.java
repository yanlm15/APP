package com.bignewsmaker.makebignews.FunctionActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bignewsmaker.makebignews.ConstData;
import com.bignewsmaker.makebignews.LIST;
import com.bignewsmaker.makebignews.News;
import com.bignewsmaker.makebignews.R;
import com.bignewsmaker.makebignews.Speaker;

/**
 * Created by liminyan on 06/09/2017.
 * 交给老陈，负责将新闻分享到微信QQ之类
 * 这里由于这个news的实现不是我实现的所以具体要到问题直接找老张
 * 他的LIST是我们原来设计的News
 * 他的News相当于我们原来的Newsmanager
 */

public class ShareActivity extends AppCompatActivity {

    private ConstData const_data = ConstData.getInstance();// 设置访问全局变量接口
    private Speaker speaker = Speaker.getInstance();// 设置语音系统接口
    private LIST list;
    public void setNews(LIST list) {
        this.list = list;
    }

    private void first_init()
    {
        setNews(const_data.getCur_list());
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        first_init(); // 获取当前新闻信息
        //设置输入监控
        //设置更新函数

    }
}
