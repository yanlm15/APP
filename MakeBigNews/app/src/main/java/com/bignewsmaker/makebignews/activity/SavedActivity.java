package com.bignewsmaker.makebignews.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bignewsmaker.makebignews.R;
import com.bignewsmaker.makebignews.adapter.NewsAdapter;
import com.bignewsmaker.makebignews.basic_class.ConstData;
import com.bignewsmaker.makebignews.basic_class.News;
import com.bignewsmaker.makebignews.basic_class.NewsList;
import com.bignewsmaker.makebignews.extra_class.RetrofitTool;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.bignewsmaker.makebignews.activity.MainActivity.setStatusBarColor;

public class SavedActivity extends AppCompatActivity {

    private int lastVisibleItem = 0;
    private Toolbar toolbar;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private List<News> listnews = new ArrayList<News>();
    private NewsList newsList = new NewsList();
    private Context mContext;
    private ConstData const_data = ConstData.getInstance();// 设置访问全局变量接口
    private RetrofitTool retrofitTool = RetrofitTool.getInstance();//设置接收器
    private String id_list[];
    private int count;
    private int category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);
        setStatusBarColor(SavedActivity.this, Color.parseColor("#303F9F"));
        toolbar = (Toolbar) findViewById(R.id.toolbar_saved);
        toolbar.setTitle("我的收藏");
        setSupportActionBar(toolbar);

        mContext = getApplicationContext();
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_saved);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //收藏上限
        id_list = new String[20];
        category = 0;
        SharedPreferences sharedPreferences = getSharedPreferences("saved_news_id_list", Context.MODE_PRIVATE); //私有数据
        Map<String, ?> allContent = sharedPreferences.getAll();
        //注意遍历map的方法
        count = 0;
        for (Map.Entry<String, ?> entry : allContent.entrySet()) {
            if (!entry.getValue().equals("")) {
                id_list[count] = entry.getKey();
                count++;
            }
        }
        System.out.println("收藏新闻ID： " + id_list[0]);
        loadNews();
       /* recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (adapter.isFadeTips() == false && lastVisibleItem + 1 == adapter.getItemCount()) {
//                        loadMoreNews();
                        adapter.setHasMore(false);
                        adapter.setFadeTips(true);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!const_data.getDay()) {
            recyclerView.setBackgroundColor(Color.rgb(66, 66, 66));
        } else {
            recyclerView.setBackgroundColor(Color.rgb(255, 255, 255));
        }
    }

    private NewsAdapter.OnItemClickListener mOnItemClickListener = new NewsAdapter.OnItemClickListener() {
        @Override
        public void onClick(View view, int position) {
            Intent i = new Intent(SavedActivity.this, ShowNewsActivity.class);
            String id = adapter.getNews(position).getNews_ID();
            const_data.setCur_ID(id);
            startActivityForResult(i, 2);
        }
    };

    private void loadNews() {

        int cnt = 0;
        SharedPreferences shared = getSharedPreferences("saved_news_num", Context.MODE_PRIVATE);
        cnt = shared.getInt("num", 0);
        System.out.println("总共收藏了  " + cnt + "   条新闻");
        if (cnt > 0) {
            System.out.println(">>1");

            for (int k = 0; k < cnt; k++) {
                try {
                    //Student对象反序列化过程
                    System.out.println("###收藏###" + k + id_list[k]);

                    System.out.println(">1>");
                    FileInputStream fis = mContext.openFileInput(id_list[k] + ".txt");

                    System.out.println("2");

                    ObjectInputStream ois = new ObjectInputStream(fis);
                    System.out.println("3");

                    News newsmy = new News();
                    newsmy.setNews_ID(id_list[k]);
                    News news = (News) ois.readObject();
                    System.out.println("33");

                    listnews.add(news);

                    System.out.println(" >> " + listnews.get(0).getNews_Title());
                    ois.close();
                    fis.close();
                    adapter = new NewsAdapter(listnews, mContext, false);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setAdapter(adapter);
                    adapter.setOnItemClickListener(mOnItemClickListener);
                    adapter.setFadeTips(true);
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(">" + e);
                }
            }
        } /*else {
            NetService service = retrofitTool.getRetrofit().create(NetService.class);
            Map<String, String> url = new HashMap<String, String>() {{
                put("keyword", "123456789");
                put("pageNo", String.valueOf(newsList == null ? 1 : newsList.getPageNo() + 1));
                put("pageSize", const_data.getCur_pageSize());
            }};
            Call<NewsList> repos = service.listReposbymap("search", url);
            repos.enqueue(new Callback<NewsList>() {
                @Override
                public void onResponse(Call<NewsList> call, Response<NewsList> response) {
                    if (response.isSuccessful()) {
                        newsList = response.body();
                    }
                }

                @Override
                public void onFailure(Call<NewsList> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }*/


    }

}
