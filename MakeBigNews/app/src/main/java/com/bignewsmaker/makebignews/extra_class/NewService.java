package com.bignewsmaker.makebignews.extra_class;

import com.bignewsmaker.makebignews.LIST;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by liminyan on 07/09/2017.
 */

public interface NewService {
    @GET("news/action/query/detail")
    Call<MyNews> listRepos(@Query("newsID")String id);
}
