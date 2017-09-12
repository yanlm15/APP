package com.bignewsmaker.makebignews.activity;

import android.content.Intent;
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
    private String totalrecords;
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
        loadMoreNews();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    if (adapter.isFadeTips() == false && lastVisibleItem + 1 == adapter.getItemCount()) {
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

    private void loadMoreNews() {
       /* new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                loadNews();
//                Message msg = new Message();
//                loadmore.sendMessage(msg);
            }
        }).start();*/
        loadNews();
//        Message msg = new Message();
//        loadmore.sendMessage(msg);
    }


    Handler loadmore = new Handler() {
        public void handleMessage(Message msg) {
//            if (newsList == null || newsList.getList().size() == 0) {
//                adapter.setHasMore(false);
//                adapter.setFadeTips(true);
//            } else {
                if (newsList.getList().size() < Integer.parseInt(const_data.getCur_pageSize())) {
                    adapter.setHasMore(false);
                    adapter.setFadeTips(true);
                }
                adapter.add(newsList.getList());
//            }
            adapter.notifyDataSetChanged();
            getSupportActionBar().setTitle("搜索结果(共" + totalrecords + "条新闻）");
        }
    };


    private void loadNews() {
        NetService service = retrofitTool.getRetrofit().create(NetService.class);
        Map<String, String> url = new HashMap<String, String>() {{
            put("keyword", keyword);
            put("pageNo", String.valueOf(newsList == null ? 1 : newsList.getPageNo() + 1));
            put("pageSize",const_data.getCur_pageSize());
        }};
        Call<NewsList> repos = service.listReposbymap("search",url);
        repos.enqueue(new Callback<NewsList>() {
            @Override
            public void onResponse(Call<NewsList> call, Response<NewsList> response) {
                if (response.isSuccessful()) {
                    newsList = response.body();
                    adapter.add(newsList.getList());
                    adapter.notifyDataSetChanged();
                    totalrecords=String.valueOf(newsList.getTotalRecords());
                    getSupportActionBar().setTitle("搜索结果(共" + totalrecords + "条新闻）");
                }
            }

            @Override
            public void onFailure(Call<NewsList> call, Throwable t) {

            }
        });

        totalrecords=String.valueOf(newsList.getTotalRecords());

        /*String s = "";
        try {
            HttpURLConnection conn;
            String pageNo = String.valueOf(newsList == null ? 1 : newsList.getPageNo() + 1);
            //http://166.111.68.66:2042/news/action/query/search?keyword=%E6%9D%AD%E5%B7%9E&pageNo=1&pageSize=10
            String url = "http://166.111.68.66:2042/news/action/query/search?keyword=" + keyword + "&pageNo=" + pageNo +
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
            totalrecords=String.valueOf(newsList.getTotalRecords());
            System.out.println("><");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("><");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(":><");
        }*/


    }

}
