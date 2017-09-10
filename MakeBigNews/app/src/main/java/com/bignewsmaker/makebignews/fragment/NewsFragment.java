package com.bignewsmaker.makebignews.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignewsmaker.makebignews.R;
import com.bignewsmaker.makebignews.activity.ShowNewsActivity;
import com.bignewsmaker.makebignews.adapter.NewsAdapter;
import com.bignewsmaker.makebignews.basic_class.ConstData;
import com.bignewsmaker.makebignews.basic_class.NewsList;
import com.bignewsmaker.makebignews.extra_class.Speaker;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NewsFragment extends Fragment {

    private int lastVisibleItem = 0;

    private NewsList newsList;
    private int category;
    private LinearLayoutManager mLayoutManager;
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
    public static NewsFragment newInstance(int category) {
        Bundle bundle = new Bundle();
        bundle.putInt("category", category);

        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private NewsAdapter.OnItemClickListener mOnItemClickListener = new NewsAdapter.OnItemClickListener() {
        @Override
        public void onClick(View view, int position) {
            Intent i = new Intent(getContext(), ShowNewsActivity.class);
            String id = newsList.getList().get(position).getNews_ID();
            const_data.setCur_ID(id);
            const_data.addHaveRead(id);
            startActivityForResult(i, 2);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        category = getArguments().getInt("category");
        view = inflater.inflate(R.layout.fragment_news, container, false);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

//        recyclerView.addOnScrollListener(mOnScrollListener);
        if (!const_data.isModel_day())
            recyclerView.setBackgroundColor(Color.rgb(66, 66, 66));
        else
            recyclerView.setBackgroundColor(Color.rgb(255, 255, 255));

        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setRefreshing(true);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.
                OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(true);
                newsList = null;
                refreshNews();
            }
        });
        refreshNews();

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

        return view;
    }

    private void refreshNews() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                loadNews();
                Message msg = new Message();
                refresh.sendMessage(msg);
            }
        }).start();
    }

    Handler refresh = new Handler() {
        public void handleMessage(Message msg) {
            adapter = new NewsAdapter(newsList.getList(), getActivity().getApplicationContext(),true);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(mOnItemClickListener);
            swipeRefresh.setRefreshing(false);
        }
    };

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
            adapter.add(newsList.getList());
//            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            swipeRefresh.setRefreshing(false);
        }
    };

    private void loadNews() {
        String s = "";
        try {
            HttpURLConnection conn;
            String pageNo = String.valueOf(newsList == null ? 1 : newsList.getPageNo() + 1);
            String url = "http://166.111.68.66:2042/news/action/query/latest?pageNo=" + pageNo +
                    "&pageSize=" + const_data.getCur_pageSize() + (category == 0 ? "" : "&category=" + category);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 2:
                adapter.notifyDataSetChanged();
        }

    }
}

