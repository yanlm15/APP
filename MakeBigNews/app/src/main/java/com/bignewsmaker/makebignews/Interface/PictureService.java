package com.bignewsmaker.makebignews.Interface;


import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by liminyan on 12/09/2017.
 */

public interface PictureService {
    @GET("search/flip")
    Call<String> getHtm(@QueryMap Map<String,String> map);
}
//&ipn=r ct=201326592