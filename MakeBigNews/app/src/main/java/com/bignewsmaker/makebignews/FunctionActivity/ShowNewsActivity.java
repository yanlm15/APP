package com.bignewsmaker.makebignews.FunctionActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bignewsmaker.makebignews.Functiontool.ConstData;
import com.bignewsmaker.makebignews.Functiontool.RetrofitTool;
import com.bignewsmaker.makebignews.R;
import com.bignewsmaker.makebignews.Functiontool.Speaker;
import com.bignewsmaker.makebignews.ThemeManager;
import com.bignewsmaker.makebignews.extra_class.Item1;
import com.bignewsmaker.makebignews.extra_class.MyNews;
import com.bignewsmaker.makebignews.extra_class.NewService;
import com.bignewsmaker.makebignews.extra_class.UrlService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
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



public class ShowNewsActivity extends AppCompatActivity implements ThemeManager.OnThemeChangeListener {

    private ConstData const_data = ConstData.getInstance();// 设置访问全局变量接口
    private Speaker speaker = Speaker.getInstance();// 设置语音系统接口
    private RetrofitTool retrofitTool = RetrofitTool.getInstance();
    private String id;
    private ArrayList<String> picture = new ArrayList<String >();
    private TreeSet<Bitmap> mybitmap = new TreeSet<>();

    private boolean isDay = const_data.isModel_day();
    private LinearLayout linearLayout;
    private Toolbar toolbar;
    private TextView textView, textView2;
    private MenuItem item_voice, item_stop, item_day, item_night;
    private boolean isread = false;



    public void setNews(String id) {
        this.id = id;
    }

    private void first_init()
    {
        setNews(const_data.getCur_ID());
    }

    private void picture_init(String str){//获取所有图片
        String s = str;
        Pattern p = Pattern.compile("http://(\\w+|\\/|\\.|-)*.(jpg|png)");
        Matcher m = p.matcher(s);

        while( m.find())
            picture.add(m.group());
        for (String e : picture) {
             System.out.println(e);

        }

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

    public void getpicture(final String url)
    {
        UrlService service = retrofitTool.getRetrofit().create(UrlService.class);

        Call<ResponseBody> urepos = service.downloadPicFromNet(url);

        urepos.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful())
                {
//                    File file = new  File(getExternalFilesDir(null) + File.separator+"picture"+File.separator+ url);


                        writeResponseBodyToDisk(response.body(), url);
//                        BitmapFactory.decodeFile(getExternalFilesDir(null) + File.separator + url);

                    System.out.println("Url-Success");
                }else {
                    System.out.println("Url-err");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("Url-f-err");
            }
        });

    }

    private boolean writeResponseBodyToDisk(ResponseBody body,String name)
    {

        try{
        File futureStudioIconFile = new File(getExternalFilesDir(null) + File.separator+"picture"+File.separator+ name);
        InputStream inputStream = null;
        OutputStream outputStream = null;


        try {
            byte[] fileReader = new byte[4096];

            long fileSize = body.contentLength();
            long fileSizeDownloaded = 0;

            inputStream = body.byteStream();
            outputStream = new FileOutputStream(futureStudioIconFile);
            while (true)
            {
                int read = inputStream.read(fileReader);

                if (read == -1){
                    break;
                }

                outputStream.write(fileReader,0,read);

                fileSizeDownloaded += read;

            }
            outputStream.flush();
            return true;

        }catch (Exception e)
        {
            return false;
        }finally {
            if (inputStream != null){
                inputStream.close();
            }

            if (outputStream != null){
                outputStream.close();
            }
        }}catch (IOException e){
        return  false;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_news);
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        ThemeManager.registerThemeChangeListener(this);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);

        first_init(); // 获取当前新闻信息
        //设置输入监控
        //设置更新函数

        System.out.println(">asd");

        NewService service = retrofitTool.getRetrofit().create(NewService.class);

        System.out.println("asd<");
        Call<MyNews> repos = service.listRepos(id);

        repos.enqueue(new Callback<MyNews>() {
            @Override
            public void onResponse(Call<MyNews> call, Response<MyNews> response) {

                if (response.isSuccessful())
                {
                    System.out.println("news-success");
                    MyNews data = new MyNews();
                    data = response.body();
                    if (data != null)
                    {
                        TextView et1 = (TextView) findViewById(R.id.textView);//content
                        TextView et2 = (TextView) findViewById(R.id.textView2);//title

                        picture_init(data.getNews_Pictures());

                        for (String e:picture)
                        {
                            System.out.println(e);//用于测试输出
                        }

                            System.out.println(data.getNews_Pictures());

                        et2.setText(data.getNews_Title());
                        et1.setText(data.getNews_Content());
                        String myt = data.getNews_Title() + "," + data.getNews_Content();
                        speaker.setText(myt);
                        ArrayList<Item1> a = data.getKeywords();
                        for (Item1 i : a)//添加关键词
                        {
                            const_data.setLike(i.word);
//                            System.out.println(i.word);//用于测试输出
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



//        System.out.println("kk><");
//        System.out.println(const_data.getCur_ID());



        init_model();    //设置夜间or日间模式
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_shownews, menu);
        item_voice = menu.findItem(R.id.voice);
        item_stop = menu.findItem(R.id.stop);
        item_day = menu.findItem(R.id.day);
        item_night = menu.findItem(R.id.night);
        if ( isread )
        {
            item_voice.setVisible(false);
            item_stop.setVisible(true);
        }
        else
        {
            item_stop.setVisible(false);
            item_voice.setVisible(true);
        }
        if ( isDay )
        {
            item_day.setVisible(false);
            item_night.setVisible(true);
        }
        else
        {
            item_night.setVisible(false);
            item_day.setVisible(true);
        }
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
            case R.id.voice:
                voice_begin();
                break;
            case R.id.stop:
                voice_stop();
                break;
            case R.id.night:
                night_model();
                break;
            case R.id.day:
                day_model();
                break;
            default:
                Toast.makeText(this, "方法还没定义", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }

    public void voice_begin(){
        speaker.setCur(this);
        speaker.start();
        isread = true;
        this.invalidateOptionsMenu();
    }
    public void voice_stop(){
        speaker.stop();
        isread = false;
        this.invalidateOptionsMenu();
    }

    public void night_model(){
        ThemeManager.setThemeMode(ThemeManager.ThemeMode.NIGHT);
        isDay = false;
        this.invalidateOptionsMenu();
        const_data.setModel_day(isDay);
    }

    public void day_model(){
        ThemeManager.setThemeMode(ThemeManager.ThemeMode.DAY);
        isDay = true;
        this.invalidateOptionsMenu();
        const_data.setModel_day(isDay);
    }

    public void init_model() {
        if (!isDay) {
            ThemeManager.setThemeMode(ThemeManager.ThemeMode.NIGHT);
            this.invalidateOptionsMenu();
        } else {
            ThemeManager.setThemeMode(ThemeManager.ThemeMode.DAY);
            this.invalidateOptionsMenu();
        }
    }
    @Override
    public void onThemeChanged() {
        //日间模式下的颜色
        textView.setTextColor(getResources().getColor(ThemeManager.getCurrentThemeRes(ShowNewsActivity.this, R.color.textColor)));
        textView2.setTextColor(getResources().getColor(ThemeManager.getCurrentThemeRes(ShowNewsActivity.this, R.color.textColor)));
        linearLayout.setBackgroundColor(getResources().getColor(ThemeManager.getCurrentThemeRes(ShowNewsActivity.this, R.color.backgroundColor)));
        toolbar.setTitleTextColor(getResources().getColor(ThemeManager.getCurrentThemeRes(ShowNewsActivity.this, R.color.titleColor)));
        toolbar.setBackgroundColor(getResources().getColor(ThemeManager.getCurrentThemeRes(ShowNewsActivity.this, R.color.toolColor)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ThemeManager.unregisterThemeChangeListener(this);
    }
}
