package com.bignewsmaker.makebignews.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
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
import android.widget.Toast;

import com.bignewsmaker.makebignews.R;
import com.bignewsmaker.makebignews.adapter.FragmentNewsAdapter;
import com.bignewsmaker.makebignews.basic_class.ConstData;
import com.bignewsmaker.makebignews.basic_class.ConstDataForSave;
import com.bignewsmaker.makebignews.basic_class.News;
import com.bignewsmaker.makebignews.extra_class.RetrofitTool;
import com.bignewsmaker.makebignews.extra_class.Speaker;
import com.bignewsmaker.makebignews.fragment.NewsFragment;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import com.iflytek.cloud.SpeechConstant;
//import com.iflytek.cloud.SpeechUtility;
/*
*重载拖动事件实现拖动更新
*/

public class MainActivity extends AppCompatActivity {
    //退出时间

    private long currentBackPressedTime = 0;

//退出间隔

    private static final int BACK_PRESSED_INTERVAL = 2000;
    private static final String TAG = "makebignews";
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<Fragment> mFragments;
    private DrawerLayout mDrawerLayout;
    private FragmentNewsAdapter adapter;
    private NavigationView navView;
    private SearchView mSearchView;
    private ConstData const_data = ConstData.getInstance();// 设置访问全局变量接口
    private Speaker speaker = Speaker.getInstance();// 设置语音系统接口
    private RetrofitTool retrofitTool = RetrofitTool.getInstance();//设置接收器


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (DataSupport.isExist(ConstDataForSave.class) && const_data.isFirstCreate()) {
            ConstDataForSave cdfs = DataSupport.findLast(ConstDataForSave.class);

            for (String e : cdfs.getFiltered())
                const_data.addFiltered(e);

            HashMap<String, News> hm = new HashMap<>();
            for (String id : cdfs.getHaveRead())
                hm.put(id, null);
            const_data.setHaveRead(hm);

            for (String e : cdfs.getDislike())
                const_data.addDislike(e);

            const_data.setDay(cdfs.isDay());
            const_data.setShow_picture(cdfs.isShow_picture());

            int i=0;
            for (Integer e:cdfs.getIstagselected()){
                const_data.setIstagSelected(i,(i==0&&e==0)?true:(e==0?false:true));
                i++;
            }

            HashMap<String, Integer> tm = new HashMap<>();
            for (String e :cdfs.getLike())
                tm.put(e,((Double)(Math.random()*100)).intValue());
            const_data.setLike(tm);
            const_data.setFirstCreate(false);
            const_data.setCur_pageSize(cdfs.getCur_pageSize()!=null&&Integer.valueOf(cdfs.getCur_pageSize())!=0?cdfs.getCur_pageSize():"20");
            const_data.setIsDownload(cdfs.getIsDownload());
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SpeechUtility.createUtility(this.getBaseContext(), SpeechConstant.APPID + "=59aa4e19");

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        navView.setCheckedItem(R.id.nav_home);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mFragments = new ArrayList<>();
        int j = 0;
        for (int i = 0; i < 14; i++) {
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
            mDrawerLayout.setBackgroundColor(Color.rgb(66, 66, 66));
        } else {
            mTabLayout.setTabTextColors(Color.rgb(0, 0, 0), Color.rgb(63, 81, 181));
            mTabLayout.setSelectedTabIndicatorColor(Color.rgb(255, 255, 255));
            mDrawerLayout.setBackgroundColor(Color.rgb(255,255 , 255));

        }

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(false);//设置选项是否选中
                item.setCheckable(true);
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        item.setChecked(true);
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

    @Override
    protected void onResume() {
        super.onResume();
        navView.setCheckedItem(R.id.nav_home);
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
//            super.onBackPressed();
            if (System.currentTimeMillis() - currentBackPressedTime > BACK_PRESSED_INTERVAL) {
                currentBackPressedTime = System.currentTimeMillis();
                Toast.makeText(this, "再按一次返回键退出程序", Toast.LENGTH_SHORT).show();
            } else {
                const_data.setExit(true);
                finish();
            }
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
                if (queryText == null) {
                    return false;
                } else {
                    const_data.setSearch_message(queryText);
                    if (!checkNetworkState()) {
                        Toast.makeText(MainActivity.this, "网络不可用，无法搜索", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);// 新建一个界面
                    startActivity(intent);
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

    private boolean checkNetworkState() {
        boolean flag = false;
        //得到网络连接信息
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        //去进行判断网络是否连接
        if (manager.getActiveNetworkInfo() != null) {
            flag = manager.getActiveNetworkInfo().isAvailable();
        }
        const_data.setConnect(flag);
        return flag;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(const_data.isExit()){
            const_data.setExit(false);
            ConstDataForSave cdfs;
            if (DataSupport.isExist(ConstDataForSave.class))
                cdfs = DataSupport.findLast(ConstDataForSave.class);
            else
                cdfs = new ConstDataForSave();
            cdfs.setHaveRead(const_data.getHaveRead());
            cdfs.setDislike(const_data.getDislike());
            cdfs.setFiltered(const_data.getFiltered());
            cdfs.setDay(const_data.getDay());
            cdfs.setShow_picture(const_data.getShow_picture());

            List<Integer> sb=new ArrayList<>();
            for (int i = 0; i < 14; i++)
                sb.add(const_data.getIstagSelected(i)==true?1:0);
            cdfs.setIstagselected(sb);

            cdfs.setLike(const_data.getLikeWord());
            cdfs.setCur_pageSize(const_data.getCur_pageSize());
            cdfs.setIsDownload(const_data.getIsDownload());
            cdfs.save();
            cdfs.updateAll();
        }
    }
}


