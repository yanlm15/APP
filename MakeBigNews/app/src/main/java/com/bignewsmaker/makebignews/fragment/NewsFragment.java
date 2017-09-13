package com.bignewsmaker.makebignews.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bignewsmaker.makebignews.Interface.NetService;
import com.bignewsmaker.makebignews.R;
import com.bignewsmaker.makebignews.activity.ShowNewsActivity;
import com.bignewsmaker.makebignews.adapter.NewsAdapter;
import com.bignewsmaker.makebignews.basic_class.ConstData;
import com.bignewsmaker.makebignews.basic_class.News;
import com.bignewsmaker.makebignews.basic_class.NewsList;
import com.bignewsmaker.makebignews.extra_class.LogicTool;
import com.bignewsmaker.makebignews.extra_class.RetrofitTool;
import com.bignewsmaker.makebignews.extra_class.Speaker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFragment extends Fragment {
    private static final String TAG = "makebignews";
    private int lastVisibleItem = 0;
    private NewsList newsList;
    private int category;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private NewsAdapter adapter;
    private View view;
    private ConstData const_data = ConstData.getInstance();// 设置访问全局变量接口
    private LogicTool logic_tool = LogicTool.getInstance();
    private Speaker speaker = Speaker.getInstance();// 设置语音系统接口
    private RetrofitTool retrofitTool = RetrofitTool.getInstance();//设置接收器

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

    @Override
    public void onResume() {
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

            News cur = adapter.getNews(position);
            if (!checkNetworkState() && !const_data.getHaveRead().contains(cur.getNews_ID())) {
                Toast.makeText(getActivity(), "网络不可用，您可查看已保存或已阅读过的新闻", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent i = new Intent(getContext(), ShowNewsActivity.class);
            const_data.setCur_news(cur);
            const_data.setCur_ID(cur.getNews_ID());
            startActivityForResult(i, 2);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        category = getArguments().getInt("category");
        int i, temp = 0;
        for (i = 0; i < 12; i++) {
            if (const_data.getIstagSelected(i))
                temp++;
            if (temp == category + 1)
                break;
        }
        category = i;
        view = inflater.inflate(R.layout.fragment_news, container, false);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setRefreshing(true);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.
                OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(true);
                newsList = null;
                if (!checkNetworkState()) {
                    Toast.makeText(getActivity(), "网络不可用，无法刷新", Toast.LENGTH_SHORT).show();
                    swipeRefresh.setRefreshing(false);
                    return;
                }
                loadNews(true, null, null);
            }
        });
        if (!checkNetworkState()) {
            if (category == 0)
                Toast.makeText(getActivity(), "网络不可用，无法加载新闻", Toast.LENGTH_SHORT).show();
            swipeRefresh.setRefreshing(false);
        } else
            loadNews(true, null, null);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (adapter.isFadeTips() == false && lastVisibleItem + 1 == adapter.getItemCount()) {
                        if (!checkNetworkState()) {
                            Toast.makeText(getActivity(), "网络不可用，无法加载更多", Toast.LENGTH_SHORT).show();
                            adapter.setHasMore(false);
                            adapter.setFadeTips(true);
                            adapter.notifyDataSetChanged();
                            return;
                        }
                        loadNews(false, null, null);
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

    private void loadNews(final boolean isRefresh, String tp, Map<String, String> m) {
        NetService service = retrofitTool.getRetrofit().create(NetService.class);

        if (category != 1) {
            Map<String, String> url = new HashMap<>();
            url.put("pageNo", String.valueOf(newsList == null ? 1 : newsList.getPageNo() + 1));
            url.put("pageSize", const_data.getCur_pageSize());
            if (category != 0)
                url.put("category", String.valueOf(category - 1));

            Call<NewsList> repos = service.listReposbymap("latest", url);
            repos.enqueue(new Callback<NewsList>() {
                @Override
                public void onResponse(Call<NewsList> call, Response<NewsList> response) {
                    if (response.isSuccessful()) {
                        newsList = logic_tool.filter_dislike(response.body());
                        if (isRefresh)
                            refreshNews();
                        else
                            loadMoreNews();
                    }
                }

                @Override
                public void onFailure(Call<NewsList> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            Map<String,Integer> key = const_data.getLike();
            String keyword;
            if (key.size() == 0) {
                keyword = "java java java java java";
            } else{
                List<Map.Entry<String, Integer>> list = new ArrayList<>(key.entrySet());
                Collections.sort(list,new Comparator<Map.Entry<String,Integer>>() {
                    @Override
                    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                        return -o1.getValue().compareTo(o2.getValue());
                    }
                });
                keyword=list.get(0).getKey()+" "+list.get(1).getKey()+" "+list.get(2).getKey()+" " +list.get(3).getKey();
            }
            Map<String, String> url = new HashMap<String, String>();
            url.put("keyword", keyword);
            url.put("pageNo", String.valueOf(newsList == null ? 1 : newsList.getPageNo() + 1));
            url.put("pageSize", const_data.getCur_pageSize());
            Call<NewsList> repos = service.listReposbymap("search",url);
            repos.enqueue(new Callback<NewsList>() {
                @Override
                public void onResponse(Call<NewsList> call, Response<NewsList> response) {
                    if (response.isSuccessful()) {
                        newsList = logic_tool.filter_dislike(response.body());
                        if (isRefresh)
                            refreshNews();
                        else
                            loadMoreNews();
                    }
                }

                @Override
                public void onFailure(Call<NewsList> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    private void refreshNews() {
        adapter = new NewsAdapter((newsList == null ? null : newsList.getList()), getActivity().getApplicationContext(), true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(mOnItemClickListener);
        swipeRefresh.setRefreshing(false);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 2:
                adapter.notifyDataSetChanged();
                if (const_data.isSetChanged()) {
                    const_data.setSetChanged(false);
                    getActivity().recreate();
                }
        }

    }

    private boolean checkNetworkState() {
        boolean flag = false;
        //得到网络连接信息
        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Activity.CONNECTIVITY_SERVICE);
        //去进行判断网络是否连接
        if (manager.getActiveNetworkInfo() != null) {
            flag = manager.getActiveNetworkInfo().isAvailable();
        }
        return flag;
    }
}

