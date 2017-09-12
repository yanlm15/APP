package com.bignewsmaker.makebignews.extra_class;

import com.bignewsmaker.makebignews.Interface.NetService;
import com.bignewsmaker.makebignews.Interface.SuccessCallBack;
import com.bignewsmaker.makebignews.basic_class.ConstData;
import com.bignewsmaker.makebignews.basic_class.News;
import com.bignewsmaker.makebignews.basic_class.NewsList;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liminyan on 11/09/2017.
 * 使用说明，如果想调用Logictool 需要重新写一个类继承 并且重载recall 函数
 * 当然你可以仅仅使用他的筛选功能
 * 我提供了根据最高频的词条，当前所看标题，搜索关键词的反馈函数，使用时需要注意这是一个异步请求
 *
 * 如果你单纯为了方便，你也可以通过设置type 执行不同的函数，只是这样的代码太丑陋了
 */

public class LogicTool implements SuccessCallBack<NewsList> {

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



    public static NewsList filter_dislike(NewsList old_list)//过滤dislike
    {
        NewsList new_list = new NewsList();
//
        for (News cur:old_list.getList())
        {
            boolean flag = true;
            for (String e: ConstData.getInstance().getDislike().keySet())
            {
                if (cur.getNews_Intro().contains(e))
                {
                    flag =false;
                    break;
                }
            }
            if (flag =true)
            {
                new_list.add_news(cur);
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

    protected void callData(final String str)
    {
        NetService service = retrofitTool.getRetrofit().create(NetService.class);

        Map<String, String> url = new HashMap<String, String>() {{
            put("keyword", str);
        }};
        Call<NewsList> repos = service.listReposbymap("search",url);

        repos.enqueue(new Callback<NewsList>() {
            @Override
            public void onResponse(Call<NewsList> call, Response<NewsList> response) {
                if (response.isSuccessful()) {

                    NewsList data = response.body();
                    if (data != null) {
                        System.out.println("S-T");
                        onSuccess(data);
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
                return;
            }
        });
    }

     public void onSuccess(NewsList a){}
//    {
//        const_data.setSearch_result(a);
//        int i=0;
//        for (News e : a.getList())
//        {
//            System.out.println(e.getNews_ID());
//            i++;
//            if (i>= const_number)
//                break;
//        }
//    }
}
