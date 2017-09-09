package com.bignewsmaker.makebignews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SearchView;

import com.bignewsmaker.makebignews.basic_class.ConstData;
import com.bignewsmaker.makebignews.R;
import com.bignewsmaker.makebignews.extra_class.Speaker;


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

        SearchView my = (SearchView) findViewById(R.id.searchView);

        my.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query == null)
                {
//                    System.out.println("><");
                    return false;
                }

                else
                {
                    const_data.setSearch_message(query);
                    System.out.println(query);
                    Intent intent=new Intent(SearchActivity.this,SearchResultActivity.class);// 新建一个界面
                    startActivity(intent);//跳转界面
                    return true;
                }
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        //设置输入监控
        //设置更新函数

    }
}