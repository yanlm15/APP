package com.bignewsmaker.makebignews.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bignewsmaker.makebignews.Interface.NetService;
import com.bignewsmaker.makebignews.R;
import com.bignewsmaker.makebignews.adapter.NewsAdapter;
import com.bignewsmaker.makebignews.basic_class.ConstData;
import com.bignewsmaker.makebignews.basic_class.NewsList;
import com.bignewsmaker.makebignews.extra_class.RetrofitTool;
import com.bignewsmaker.makebignews.extra_class.Speaker;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bignewsmaker.makebignews.activity.MainActivity.setStatusBarColor;

/**
 * Created by liminyan on 06/09/2017.
 * 这个界面我们讨论一下再决定要不要
 * 传入的搜索信息储存在const_data.getSearch_message();
 * 传入的分类（标签）信息储存在const_data.getSearch_class();
 */

public class SearchResultActivity extends AppCompatActivity {
    private static final String TAG = "makebignews";
    private int lastVisibleItem = 0;
    private Toolbar toolbar;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private NewsList newsList;
    private ConstData const_data = ConstData.getInstance();// 设置访问全局变量接口
    private Speaker speaker = Speaker.getInstance();// 设置语音系统接口
    private String keyword = const_data.getSearch_message();
    private RetrofitTool retrofitTool = RetrofitTool.getInstance();//设置接收器


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        setStatusBarColor(SearchResultActivity.this, Color.parseColor("#303F9F"));
        toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        newsList = new NewsList();
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view2);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new NewsAdapter(newsList.getList(), this, true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(mOnItemClickListener);
        loadNews(true);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (adapter.isFadeTips() == false && lastVisibleItem + 1 == adapter.getItemCount())
                        loadNews(false);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        if (!const_data.getDay()) {
            recyclerView.setBackgroundColor(Color.rgb(66, 66, 66));
        } else {
            recyclerView.setBackgroundColor(Color.rgb(255, 255, 255));
        }
    }

    private NewsAdapter.OnItemClickListener mOnItemClickListener = new NewsAdapter.OnItemClickListener() {
        @Override
        public void onClick(View view, int position) {
            Intent i = new Intent(SearchResultActivity.this, ShowNewsActivity.class);
            String id = adapter.getNews(position).getNews_ID();
            const_data.setCur_ID(id);
//            const_data.addHaveRead(adapter.getNews(position));
            startActivityForResult(i, 2);
        }
    };

    private void loadNews(final boolean isFirst) {
        NetService service = retrofitTool.getRetrofit().create(NetService.class);
        Map<String, String> url = new HashMap<>();
        url.put("keyword", keyword);
        url.put("pageNo", String.valueOf(newsList == null ? 1 : newsList.getPageNo() + 1));
        url.put("pageSize", const_data.getCur_pageSize());
        Call<NewsList> repos = service.listReposbymap("search", url);
        repos.enqueue(new Callback<NewsList>() {
            @Override
            public void onResponse(Call<NewsList> call, Response<NewsList> response) {
                if (response.isSuccessful()) {
                    newsList = response.body();
                    if (isFirst)
                        getSupportActionBar().setTitle("搜索结果(共" + newsList.getTotalRecords() + "条新闻）");
                    loadMoreNews();
                }
            }

            @Override
            public void onFailure(Call<NewsList> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void loadMoreNews() {
        if (newsList == null || newsList.getList().size() == 0) {
            adapter.setHasMore(false);
            adapter.setFadeTips(true);
        } else {
            if (newsList.getList().size() < Integer.parseInt(const_data.getCur_pageSize())) {
                adapter.setHasMore(false);
                adapter.setFadeTips(true);
            }
            adapter.add(newsList.getList());
        }
        adapter.notifyDataSetChanged();
    }
}
