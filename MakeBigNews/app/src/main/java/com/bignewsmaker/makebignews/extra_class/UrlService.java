package com.bignewsmaker.makebignews.extra_class;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by liminyan on 09/09/2017.
 */

public interface UrlService {

    //下载图像
    @GET
    Call<ResponseBody> downloadPicFromNet(@Url String fileUrl);

}