package com.bignewsmaker.makebignews.FunctionActivity;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bignewsmaker.makebignews.ConstData;
import com.bignewsmaker.makebignews.LIST;
import com.bignewsmaker.makebignews.News;
import com.bignewsmaker.makebignews.R;
import com.bignewsmaker.makebignews.Speaker;
import com.bignewsmaker.makebignews.extra_class.Item1;
import com.bignewsmaker.makebignews.extra_class.MyNews;
import com.bignewsmaker.makebignews.extra_class.NewService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import retrofit2.converter.gson.*;

/**
 * Created by liminyan on 06/09/2017.
 * 用于显示单条新闻
 * 通过访问设置参数决定显示的模式
 * 这里就体现了参数传递的问题，你想通过一个界面给另一个界面传或者接受复杂参数是很麻烦的，
 * 除非你重写Intent 或者startActivit，
 * 但是对于全局变量，更推荐我们这种写法
 * 所以我们采用的是const_data 方法来传递参数，
 * --举个例子
 * |-你可以将当前选中的news存到const_data，然后在新的activity中直接调用const_data
 * |-当然你可以参考https://www.2cto.com/kf/201311/256174.html 对比其他的方法
 */

public class ShowNewsActivity extends AppCompatActivity {

    private ConstData const_data = ConstData.getInstance();// 设置访问全局变量接口
    private Speaker speaker = Speaker.getInstance();// 设置语音系统接口
    private String id;
//    MyNews data = new MyNews();
    public void setNews(String id) {
        this.id = id;
    }

    private void first_init()
    {
        setNews(const_data.getCur_ID());
    }



    void setHighLight(String str) //高亮关键字
    {

    }
    void setURL(String str) //关键字超链接
    {

    }
    void setPicture(String str)// html 图片解析
    {

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        first_init(); // 获取当前新闻信息
        //设置输入监控
        //设置更新函数

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://166.111.68.66:2042/")
                .build();
        NewService service = retrofit.create(NewService.class);
        Call<MyNews> repos = service.listRepos(id);

        repos.enqueue(new Callback<MyNews>() {
            @Override
            public void onResponse(Call<MyNews> call, Response<MyNews> response) {
//
                if (response.isSuccessful())
                {
                    MyNews data = new MyNews();
                    data = response.body();
                    if (data != null)
                    {
                        TextView et1 = (TextView) findViewById(R.id.textView);
                        TextView et2 = (TextView) findViewById(R.id.textView2);
                        et2.setText(data.getNews_Title());
                        et1.setText(data.getNews_Content());

                        ArrayList<Item1> a = data.getKeywords();
                        for (Item1 i : a)//添加关键词
                        {
                            const_data.setLike(i.word);
                            System.out.println(i.word);//用于测试输出
                        }

                    }
                }
                else
                {
                    System.out.println("fuck");
                }

            }

            @Override
            public void onFailure(Call<MyNews> call, Throwable t) {
                t.printStackTrace();

            }
        });



//        et1.setText(data.getNews_Title());
        System.out.println("kk");

//        System.out.println(data.keywords[0].);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_shownews, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void shareMsg(String activityTitle, String msgTitle, String msgText, String imgPath ){
        Intent intent = new Intent(Intent.ACTION_SEND);
        if ( imgPath == null || imgPath.equals("")){
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT,msgTitle);
            intent.putExtra(Intent.EXTRA_TEXT,msgText);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Intent.createChooser(intent, activityTitle));
        }
        else{
            File f = new File(imgPath);
            if( f != null && f.exists() && f.isFile() ){
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,msgText);
                intent.setType("image/*");
                Uri u = Uri.fromFile(f);
                intent.putExtra(Intent.EXTRA_STREAM, u);

                intent.putExtra(Intent.EXTRA_SUBJECT,msgTitle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, activityTitle));
            }
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                shareMsg("分享图片和文字", "Share", "img and text", null);
                return true;
            default:
                Toast.makeText(this, "方法还没定义", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }
}
