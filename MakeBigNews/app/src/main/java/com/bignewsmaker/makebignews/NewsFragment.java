package com.bignewsmaker.makebignews;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class NewsFragment extends Fragment {
    private News news;
    private int category;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private NewsAdapter adapter;
    private View view;
    private ConstData const_data = ConstData.getInstance();// 设置访问全局变量接口
    private Speaker speaker = Speaker.getInstance();// 设置语音系统接口

    public NewsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(int index) {
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);

        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_news, container, false);

        category = getArguments().getInt("index");
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        refreshNews();


        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setRefreshing(true);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.
                OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshNews();
            }
        });


        return view;
    }


    private void refreshNews() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                loadNews();
                Message msg = new Message();
                handler.sendMessage(msg);

            }

        }).start();
    }


    private void loadNews() {

        String s = "";
        try {


            HttpURLConnection conn;
            int pageNo;
            if (news == null)
                pageNo = 1;
            else
                pageNo = news.getPageNo() + 1;
            URL url;
            if (category == 0)
                url = new URL("http://166.111.68.66:2042/news/action/query/latest?pageNo=" + pageNo + "&pageSize=20");
            else
                url = new URL("http://166.111.68.66:2042/news/action/query/latest?pageNo=" + pageNo + "&pageSize=20"+"&category="+category);
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
            news=mapper.readValue(s, News.class);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            adapter = new NewsAdapter(news.getList());
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
            swipeRefresh.setRefreshing(false);
        }
    };

}
