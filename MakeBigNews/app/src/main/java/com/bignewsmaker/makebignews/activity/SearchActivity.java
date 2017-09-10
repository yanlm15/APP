package com.bignewsmaker.makebignews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SearchView;

import com.bignewsmaker.makebignews.Interface.SearchService;
import com.bignewsmaker.makebignews.basic_class.ConstData;
import com.bignewsmaker.makebignews.R;
import com.bignewsmaker.makebignews.basic_class.News;
import com.bignewsmaker.makebignews.basic_class.NewsList;
import com.bignewsmaker.makebignews.extra_class.RetrofitTool;
import com.bignewsmaker.makebignews.extra_class.Speaker;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by liminyan on 06/09/2017.
 * 搜索界面
 *
 * 请求的结果我放在了const—data的searchresult里面
 * 你直接用就行
 * 
 */

public class SearchActivity  extends AppCompatActivity {

    private ConstData const_data = ConstData.getInstance();// 设置访问全局变量接口
    private Speaker speaker = Speaker.getInstance();// 设置语音系统接口
    private RetrofitTool retrofitTool = RetrofitTool.getInstance();//设置接收器




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SearchView my = (SearchView) findViewById(R.id.searchView);

        my.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query == null) {
                    return false;
                }

                else
                {
                    const_data.setSearch_message(query);
                    System.out.println(query);
                    callData(query);
                    return true;
                }
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        //设置输入监控
        //设置更新函数
    }

    private void callData(String str)
    {
        SearchService service = retrofitTool.getRetrofit().create(SearchService.class);

        Call<NewsList> repos = service.listRepos(str);

        repos.enqueue(new Callback<NewsList>() {
            @Override
            public void onResponse(Call<NewsList> call, Response<NewsList> response) {
                if (response.isSuccessful()) {

                    NewsList data = response.body();
                    if (data != null) {
                        System.out.println("S-T");
                        reCall(data);
                    }
                    else {
                        System.out.println("S-F");
                    }
                }
                else {
                    System.out.println("S-F");
                }
            }

            @Override
            public void onFailure(Call<NewsList> call, Throwable t) {

            }
        });
    }

    private void reCall(NewsList a){
        const_data.setSearch_result(a);
        for (News e : a.getList())
        {
            System.out.println(e.getNews_ID());
        }
        Intent intent=new Intent(SearchActivity.this,SearchResultActivity.class);// 新建一个界面
        startActivity(intent);//跳转界面
    }
}