package com.bignewsmaker.makebignews;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
/*
*重载拖动事件实现拖动更新
*/

public class MainActivity extends AppCompatActivity {

    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private NewsAdapter adapter;
    private ConstData const_data = ConstData.getInstance();// 设置访问全局变量接口
    private Speaker speaker = Speaker.getInstance();// 设置语音系统接口

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //获得新闻
        loadNews();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NewsAdapter(const_data.getCur_news().getList());
        recyclerView.setAdapter(adapter);

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.
                OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshNews();
            }
        });
    }


    private void refreshNews(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Thread.sleep(300);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        loadNews();
                        adapter = new NewsAdapter(const_data.getCur_news().getList());
                        recyclerView.setAdapter(adapter);
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private void loadNews() {

        String s = "";
        try {
            //暂时性的解决NetworkOnMainThreadException办法
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            HttpURLConnection conn;
            int pageNo;
            if(const_data.getCur_news()==null)
                pageNo=1;
            else
                pageNo=const_data.getCur_news().getPageNo()+1;
            URL url = new URL("http://166.111.68.66:2042/news/action/query/latest?pageNo="+pageNo+"&pageSize=20");
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(60 * 1000);
            conn.setReadTimeout(30 * 1000);
            if (conn.getResponseCode() != 200)
                return;
            InputStream inputStream = conn.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            byte[] data = outputStream.toByteArray();
            s += new String(data, "utf-8");
            outputStream.close();
            inputStream.close();
            conn.disconnect();

            ObjectMapper mapper = new ObjectMapper();
            const_data.setCur_news(mapper.readValue(s, News.class));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
