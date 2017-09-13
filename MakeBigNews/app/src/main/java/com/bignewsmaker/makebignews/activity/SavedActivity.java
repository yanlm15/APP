package com.bignewsmaker.makebignews.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import static com.bignewsmaker.makebignews.activity.MainActivity.setStatusBarColor;

public class SavedActivity extends AppCompatActivity {

    private int lastVisibleItem = 0;
    private Toolbar toolbar;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private NewsList newsList;
    private Context mContext;
    private ConstData const_data = ConstData.getInstance();// 设置访问全局变量接口
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
        newsList = new NewsList();
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
        for(Map.Entry<String, ?>  entry : allContent.entrySet()){
            if ( !entry.getValue().equals("") ){
                id_list[count] = entry.getKey();
                count++;
            }
        }

        System.out.println("收藏新闻ID： " + id_list[0]);



        adapter = new NewsAdapter(newsList.getList(), this, true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(mOnItemClickListener);
        loadMoreNews();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (adapter.isFadeTips() == false && lastVisibleItem + 1 == adapter.getItemCount()) {
                        loadMoreNews();
                    }

                    if (adapter.isFadeTips() == true && lastVisibleItem + 2 == adapter.getItemCount()) {
                        loadMoreNews();
                    }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        recreate();
    }

    private void loadMoreNews() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                loadNews();




                Message msg = new Message();
                loadmore.sendMessage(msg);
            }
        }).start();
    }


    Handler loadmore = new Handler() {
        public void handleMessage(Message msg) {
            if (newsList == null || newsList.getList().size() == 0) {
                adapter.setHasMore(false);
                adapter.setFadeTips(true);
            } else {
                if (newsList.getList().size() < Integer.parseInt(const_data.getCur_pageSize())) {
                    adapter.setHasMore(false);
                    adapter.setFadeTips(true);
                }
//                adapter.add(newsList.getList());
            }
            adapter.notifyDataSetChanged();
        }
    };

    private void loadNews() {


        int cnt = 0;
        SharedPreferences shared = getSharedPreferences("saved_news_num", Context.MODE_PRIVATE);
        cnt = shared.getInt("num", 0);
        System.out.println("总共收藏了  " + cnt + "   条新闻");

        if (cnt == 0) {
            String s = "";
            try {
                HttpURLConnection conn;
                String pageNo = String.valueOf(newsList == null ? 1 : newsList.getPageNo() + 1);
                String url = "http://166.111.68.66:2042/news/action/query/search?keyword=" + "123456789" + "&pageNo=" + pageNo +
                        "&pageSize=" + const_data.getCur_pageSize();
                URL u = new URL(url);
                conn = (HttpURLConnection) u.openConnection();
                InputStream inputStream = conn.getInputStream();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = -1;
                while ((len = inputStream.read(buffer)) != -1)
                    outputStream.write(buffer, 0, len);
                byte[] data = outputStream.toByteArray();
                s += new String(data, "utf-8");
                outputStream.close();
                inputStream.close();
                conn.disconnect();
                Gson gson = new Gson();
                newsList = gson.fromJson(s, NewsList.class);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if ( cnt > 0 ){
            for ( int k = 0; k < cnt; k++ ){
                try
                {
                    //Student对象反序列化过程
                    FileInputStream fis = mContext.openFileInput(id_list[k]+".txt");
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    News news = (News) ois.readObject();

                    System.out.println("###收藏###"+k+news.getNews_ID());

                    newsList.add_news(news);
                    ois.close();
                    fis.close();
                }
                catch(ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

}
