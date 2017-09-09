package com.bignewsmaker.makebignews.extra_class;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by liminyan on 09/09/2017.
 * 请求器
 */

public class RetrofitTool {
    static private RetrofitTool cur;

    private Retrofit retrofit;

    public void setRetrofit() {
        this.retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://166.111.68.66:2042/")
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public static RetrofitTool getInstance() {
        if (cur == null)
        {

            cur = new RetrofitTool();
            cur.setRetrofit();
        }
        return cur;
    }


}
