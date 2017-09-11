package com.bignewsmaker.makebignews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bignewsmaker.makebignews.R;
import com.bignewsmaker.makebignews.adapter.NewsAdapter;
import com.bignewsmaker.makebignews.basic_class.ConstData;
import com.bignewsmaker.makebignews.basic_class.NewsList;
import com.bignewsmaker.makebignews.extra_class.Speaker;

/**
 * Created by liminyan on 06/09/2017.
 * 这个界面我们讨论一下再决定要不要
 * 传入的搜索信息储存在const_data.getSearch_message();
 * 传入的分类（标签）信息储存在const_data.getSearch_class();
 */

public class SearchResultActivity extends AppCompatActivity {
    private int lastVisibleItem = 0;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private NewsList result;
    private ConstData const_data = ConstData.getInstance();// 设置访问全局变量接口
    private Speaker speaker = Speaker.getInstance();// 设置语音系统接口

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);


        result=const_data.getSearch_result();
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view2);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {


                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });

        adapter = new NewsAdapter(result.getList(), this, result.getList().size()>0?true:false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(mOnItemClickListener);

    }

    private NewsAdapter.OnItemClickListener mOnItemClickListener = new NewsAdapter.OnItemClickListener() {
        @Override
        public void onClick(View view, int position) {
            Intent i = new Intent(SearchResultActivity.this, ShowNewsActivity.class);
            String id = adapter.getId(position);
            const_data.setCur_ID(id);
            const_data.addHaveRead(id);
            startActivityForResult(i, 2);
        }
    };




}
