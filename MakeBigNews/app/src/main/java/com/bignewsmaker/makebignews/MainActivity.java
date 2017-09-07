package com.bignewsmaker.makebignews;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
/*
*重载拖动事件实现拖动更新
*/

public class MainActivity extends AppCompatActivity {

//    LinearLayoutManager layoutManager;
//    RecyclerView recyclerView;
//    private SwipeRefreshLayout swipeRefresh;
//    private NewsAdapter adapter;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<Fragment> mFragments;

    private FragmentNewsAdapter adapter;

    private ConstData const_data = ConstData.getInstance();// 设置访问全局变量接口
    private Speaker speaker = Speaker.getInstance();// 设置语音系统接口

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);



        mFragments = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            mFragments.add(NewsFragment.newInstance(i));
        }
        adapter=new FragmentNewsAdapter(getSupportFragmentManager(),mFragments);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);


        //获得新闻
//        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        adapter = new NewsAdapter(const_data.getCur_news().getList());
//        recyclerView.setAdapter(adapter);

//        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
//        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
//        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.
//                OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshNews();
//            }
//        });
    }




}
