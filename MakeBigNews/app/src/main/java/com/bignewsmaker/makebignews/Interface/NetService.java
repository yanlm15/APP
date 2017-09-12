package com.bignewsmaker.makebignews.Interface;

import com.bignewsmaker.makebignews.basic_class.NewsList;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by liminyan on 10/09/2017.
 */

public interface NetService {

    @GET("news/action/query/{type}")
    Call<NewsList> listReposbymap(@Path("type")String type,@QueryMap Map<String,String> url);

    //@Path("type")String type,
    Call<NewsList> listRepos(@Query("keyword")String str);
    //增加序列化查找，设置keyword，&pageNo，&pageSize，&category
    //例如 Map<String,String> m m.put("keyword",XXX) m.put("pageNo",XX) m.put("pageSize",XX)
    // SearchService service = retrofitTool.getRetrofit().create(SearchService.class);
    //Call<NewsList> listReposbymap = service.listRepos(m); 即可通过相应的接口来实现更新等操作
}
