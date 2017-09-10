package com.bignewsmaker.makebignews.Interface;

import com.bignewsmaker.makebignews.basic_class.MyNews;
import com.bignewsmaker.makebignews.basic_class.NewsList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by liminyan on 10/09/2017.
 */

public interface SearchService {

    @GET("news/action/query/search")
    Call<NewsList> listRepos(@Query("keyword")String str);
}