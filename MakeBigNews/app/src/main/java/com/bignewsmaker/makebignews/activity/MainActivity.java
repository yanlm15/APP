package com.bignewsmaker.makebignews.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bignewsmaker.makebignews.Interface.SearchService;
import com.bignewsmaker.makebignews.R;
import com.bignewsmaker.makebignews.adapter.FragmentNewsAdapter;
import com.bignewsmaker.makebignews.basic_class.ConstData;
import com.bignewsmaker.makebignews.basic_class.NewsList;
import com.bignewsmaker.makebignews.extra_class.RetrofitTool;
import com.bignewsmaker.makebignews.extra_class.Speaker;
import com.bignewsmaker.makebignews.fragment.NewsFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/*
*重载拖动事件实现拖动更新
*/

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "makebignews";
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<Fragment> mFragments;
    private DrawerLayout mDrawerLayout;
    private FragmentNewsAdapter adapter;
    private SearchView mSearchView;
    private ConstData const_data = ConstData.getInstance();// 设置访问全局变量接口
    private Speaker speaker = Speaker.getInstance();// 设置语音系统接口
    private RetrofitTool retrofitTool = RetrofitTool.getInstance();//设置接收器


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
        navView.setCheckedItem(R.id.nav_home);
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
        int j = 0;
        for (int i = 0; i < 13; i++) {
            if (const_data.getIstagSelected(i)) {
                mFragments.add(NewsFragment.newInstance(j));
                j++;
            }
        }
        adapter = new FragmentNewsAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(6);
        mTabLayout.setupWithViewPager(mViewPager);

        if (!const_data.getDay()) {
            mTabLayout.setBackgroundColor(Color.rgb(66, 66, 66));
            mTabLayout.setTabTextColors(Color.rgb(128, 128, 128), Color.rgb(255, 255, 255));
            mTabLayout.setSelectedTabIndicatorColor(Color.rgb(255, 255, 255));
            toolbar.setBackgroundColor(Color.rgb(66, 66, 66));
            setStatusBarColor(MainActivity.this, Color.rgb(66, 66, 66));
        } else {
            mTabLayout.setTabTextColors(Color.rgb(0, 0, 0), Color.rgb(63, 81, 181));
        }

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(false);//设置选项是否选中
                item.setCheckable(false);//选项是否可选
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent home = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(home);
                        break;
                    case R.id.nav_saved:
                        Intent savedIntent = new Intent(MainActivity.this, SavedActivity.class);
                        startActivity(savedIntent);
                        break;
                    case R.id.nav_setting:
                        Intent setIntent = new Intent(MainActivity.this, SetActivity.class);
                        startActivityForResult(setIntent, 1);
                        break;
                    case R.id.nav_about:
                        Intent aboutIntent = new Intent(MainActivity.this, AboutActivity.class);
                        startActivity(aboutIntent);
                        break;

                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

    }

    @TargetApi(21)
    static void setStatusBarColor(Activity activity, int statusColor) {
        Window window = activity.getWindow();
        //取消状态栏透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(statusColor);
        //设置系统状态栏处于可见状态
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
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
        mSearchView = (SearchView) menu.findItem(R.id.search).getActionView();
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setSubmitButtonEnabled(true);//设置是否显示搜索按钮
        mSearchView.setQueryHint("请输入搜索内容...");//设置提示信息
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String queryText) {
                return false;
            }
            @Override
            public boolean onQueryTextSubmit(String queryText) {
               /* if (mSearchView != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0); // 输入法如果是显示状态，那么就隐藏输入法
                    }
                    mSearchView.clearFocus(); // 不获取焦点
                }*/
                if (queryText == null) {
                    return false;
                } else {
                    const_data.setSearch_message(queryText);
                    callData(queryText);
                    return true;
                }
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
           /* case R.id.search:
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                break;*/
            case R.id.setting:
                Intent setIntent = new Intent(MainActivity.this, SetActivity.class);
                startActivityForResult(setIntent, 1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (const_data.isSetChanged()) {
                    const_data.setSetChanged(false);
                    recreate();
                    break;
                }
        }
    }

    //处理搜索数据
    private void callData(String str) {
        SearchService service = retrofitTool.getRetrofit().create(SearchService.class);
        Call<NewsList> repos = service.listRepos(str);
        repos.enqueue(new Callback<NewsList>() {
            @Override
            public void onResponse(Call<NewsList> call, Response<NewsList> response) {
                if (response.isSuccessful()) {
                    NewsList data = response.body();
                    if (data != null) {
                        reCall(data);
                    }
                }
            }

            @Override
            public void onFailure(Call<NewsList> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void reCall(NewsList a) {
        const_data.setSearch_result(a);
        Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);// 新建一个界面
        startActivity(intent);//跳转界面
    }
}