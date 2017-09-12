package com.bignewsmaker.makebignews.Interface;

import com.bignewsmaker.makebignews.basic_class.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by liminyan on 07/09/2017.
 */

public interface NewsService {
    @GET("news/action/query/detail")
    Call<News> listRepos(@Query("newsId")String id);
}
