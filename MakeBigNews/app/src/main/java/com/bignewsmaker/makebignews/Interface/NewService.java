package com.bignewsmaker.makebignews.Interface;

import com.bignewsmaker.makebignews.basic_class.MyNews;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by liminyan on 07/09/2017.
 */

public interface NewService {
    @GET("news/action/query/detail")
    Call<MyNews> listRepos(@Query("newsId")String id);
}
