package com.bignewsmaker.makebignews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.bignewsmaker.makebignews.basic_class.ConstData;
import com.bignewsmaker.makebignews.adapter.FragmentNewsAdapter;
import com.bignewsmaker.makebignews.extra_class.Speaker;
import com.bignewsmaker.makebignews.fragment.NewsFragment;
import com.bignewsmaker.makebignews.R;

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
    private DrawerLayout mDrawerLayout;
    private FragmentNewsAdapter adapter;

    private ConstData const_data = ConstData.getInstance();// 设置访问全局变量接口
    private Speaker speaker = Speaker.getInstance();// 设置语音系统接口

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        navView.setCheckedItem(R.id.nav_saved);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mFragments = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            mFragments.add(NewsFragment.newInstance(i));
        }
        adapter = new FragmentNewsAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(false);//设置选项是否选中
                item.setCheckable(false);//选项是否可选
                switch (item.getItemId()) {
                    case R.id.nav_saved:

                        break;
                    case R.id.nav_setting:
                        Intent setIntent = new Intent(MainActivity.this, SetActivity.class);
                        startActivity(setIntent);
                        break;
                    case R.id.nav_about:
                        break;

                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.search:
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.setting:
                Intent setIntent = new Intent(MainActivity.this, SetActivity.class);
                startActivity(setIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
