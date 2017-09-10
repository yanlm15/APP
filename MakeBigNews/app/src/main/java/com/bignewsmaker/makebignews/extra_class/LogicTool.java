package com.bignewsmaker.makebignews.extra_class;

import android.content.Intent;

import com.bignewsmaker.makebignews.Interface.SearchService;
import com.bignewsmaker.makebignews.activity.SearchActivity;
import com.bignewsmaker.makebignews.activity.SearchResultActivity;
import com.bignewsmaker.makebignews.basic_class.ConstData;
import com.bignewsmaker.makebignews.basic_class.News;
import com.bignewsmaker.makebignews.basic_class.NewsList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liminyan on 11/09/2017.
 * 使用说明，如果想调用Logictool 必须重新写一个类继承 并且重载recall 函数
 * recall
 */

public class LogicTool {

    protected ConstData const_data = ConstData.getCur();
    protected RetrofitTool retrofitTool = RetrofitTool.getInstance();
    protected int const_number = 3;
    protected String type ="";
    static LogicTool cur;

    public void setConst_number(int const_number) {
        this.const_number = const_number;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static LogicTool getInstance() {
        if (cur == null)
            cur = new LogicTool();
        return cur;
    }

    public NewsList filter_dislike(NewsList old_list)//过滤dislike
    {
        NewsList new_list = new NewsList();
        for (String e: ConstData.getInstance().getDislike().keySet())
        {
            for (News cur:old_list.getList())
            {
                if (cur.getNews_Intro().contains(e)==false)
                {
                    new_list.add_news(cur);
                }
            }
        }
        return new_list;
    }

    public void analys_top1_keyword()//根据第一的词条推荐
    {
        NewsList res = new NewsList();
        if (ConstData.getInstance().getLike().isEmpty() == false)
        {
            String top = null;
            for (String e: ConstData.getInstance().getLike().keySet())
            {
                top = e;
                break;
            }
            callData(top);
        }
    }

    public void recommend_by_tytle(String str,int const_number)//根据当前浏览的新闻推荐
    {
        NewsList res = new NewsList();
        //call str
        callData(str);
    }

    public void recommend_by_history()//根据历史记录推荐
    {
        NewsList res = new NewsList();

    }

    protected void callData(String str)
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
                        reCall(data,const_number);
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

     protected void reCall(NewsList a,int num)
    {
        const_data.setSearch_result(a);
        int i=0;
        for (News e : a.getList())
        {
            System.out.println(e.getNews_ID());
            i++;
            if (i>= num)
                break;
        }
    }
}
