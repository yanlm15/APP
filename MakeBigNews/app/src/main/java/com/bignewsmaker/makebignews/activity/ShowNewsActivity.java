package com.bignewsmaker.makebignews.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bignewsmaker.makebignews.Interface.NewsService;
import com.bignewsmaker.makebignews.Interface.SuccessCallBack;
import com.bignewsmaker.makebignews.Interface.UrlService;
import com.bignewsmaker.makebignews.R;
import com.bignewsmaker.makebignews.basic_class.ConstData;
import com.bignewsmaker.makebignews.basic_class.Item1;
import com.bignewsmaker.makebignews.basic_class.Item2;
import com.bignewsmaker.makebignews.basic_class.News;
import com.bignewsmaker.makebignews.basic_class.NewsList;
import com.bignewsmaker.makebignews.extra_class.FileHelper;
import com.bignewsmaker.makebignews.extra_class.InternetPicturetool;
import com.bignewsmaker.makebignews.extra_class.LogicTool;
import com.bignewsmaker.makebignews.extra_class.RetrofitTool;
import com.bignewsmaker.makebignews.extra_class.Speaker;
import com.bignewsmaker.makebignews.extra_class.ThemeManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bignewsmaker.makebignews.activity.MainActivity.setStatusBarColor;

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
 * <p>
 * <p>
 * 图片获取方法 处理函数通过getpicture()请求，成功后再回调处理函数
 * 其中处理函数 需要添加判断是否本地有存储 如果有则倒入本地资源
 * 类似的思想可以应用到离线离线新闻列表中
 * 每次打开app就判读 a这个文件是否为空，是调用网络响应反馈加载，否则将文本a的数据写到界面，
 * 触发更新函数后就等待网络响应，获取新的新闻，关闭然后将最后一次获取的新闻写到a
 * 以上就是离线列表的实现思路
 * <p>
 * 设置信息储存，当然要是实现永久性，将信息写到一个文件里，然后开机的时候倒入
 * getExternalFilesDir(null)获取外部存储目录
 * <p>
 * 然后这里是Lgictool 的使用方法：采用了范型接口，新定义一个类继承 Logictool（自己也继承了SuccessCallBack）
 * 然后继承 SuccessCallBack<T>
 * 就可以在不同的activity下使用搜索（既然主页的搜索实现了，那我就不添加继续加载了），筛选，请求等功能
 * 这个例子提供的接口是搜索后的回调接口，其中a是搜索返回的结果
 * 其实你们的收藏和下载也可以用类似的方法实现，以应对不同的activity的不同需求
 */


public class ShowNewsActivity extends AppCompatActivity implements ThemeManager.OnThemeChangeListener {

    private ConstData const_data = ConstData.getInstance();// 设置访问全局变量接口
    private Speaker speaker = Speaker.getInstance();// 设置语音系统接口
    private RetrofitTool retrofitTool = RetrofitTool.getInstance();
    private NewsList mNewsList = new NewsList();
    private TextView[] m = new TextView[3];
    private int[] place = new int[]{0, 1, 2};
    private int search_number = 1;
    private boolean flag = true;
    private boolean okDownload = true;
    private int minnumber = 1;

    class MyLogicTool extends LogicTool implements SuccessCallBack<NewsList> {
        @Override
        public void onSuccess(NewsList a) {
            mNewsList = a;
            int i = 0;

            m[0] = (TextView) findViewById(R.id.r_1);
            m[1] = (TextView) findViewById(R.id.r_2);
            m[2] = (TextView) findViewById(R.id.r_3);
            for (int k = 0; k < a.getList().size(); k++) {
                News e = a.getList().get(k);

                boolean flag = true;

                if (title.equals(e.getNews_Title())) {
                    flag = false;
                }

                for (int j = 0; j < k; j++) {
                    if (a.getList().get(j).getNews_ID().equals(e.getNews_ID())) {
                        flag = false;
                        break;
                    }
                }

                if (flag == true) {
                    m[i].setText("\n" + e.getNews_Title() + "\n");
                    place[i] = k;
                    i++;
                }
                if (i > 2)
                    break;

            }
        }
    }


    class MyTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub execute
            String str = params[0];
            String jstr = "";
            try {
                jstr = InternetPicturetool.getInstance().getHTML(title);

            } catch (Exception e) {
                System.out.println(e);
            }

            return jstr;
        }

        @Override
        protected void onPostExecute(String result) {

            int i = 0;
            if (picture.size() == 0) ;
            {
                for (String e : InternetPicturetool.getInstance().getResult()) {
                    picture.add(e);
                    i++;
                    if (i >= minnumber) {
                        break;
                    }
                }
                context = clearExtra(context);
                context = jump_peo(context);
                context = setbody(context);
                context = setEmptyP(context);
                context = setP(context, 0);

                showText();
                setText(0);

            }

        }

    }

    private News getNews() {
        return myNews;
    }

    private MyLogicTool myLogicTool = new MyLogicTool();

    public void setContext_rec() {
        getContext_rec();
    }

    public void getContext_rec() {
        myLogicTool.recommend_by_tytle(myNews.getNews_Title(), 3);
    }

    private String id;
    private String title;
    private String context;
    private String context_rec;
    private News myNews = new News();
    private ArrayList<String> picture = new ArrayList<String>();
    private ArrayList<String> safepicture = new ArrayList<String>();

    private ArrayList<Bitmap> mybitmap = new ArrayList<Bitmap>();

    private boolean isDay = const_data.isModel_day();
    private LinearLayout linearLayout;
    private Toolbar toolbar;
    private MenuItem item_voice, item_stop, item_saved, item_cancel_saved, item_download, item_delete;
    private WebView et1;
    private boolean isread = false;
    private boolean isdownload = false;
    private Context mContext;
    private boolean issaved = false;
    private String news_content = "";  //新闻标题 + 内容
    private String shared_content = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_news);
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        speaker.setCur(this);

        mContext = getApplicationContext();
        et1 = (WebView) findViewById(R.id.textView);//content
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        ThemeManager.registerThemeChangeListener(this);

        first_init(); // 获取当前新闻信息
        //设置输入监控
        //设置更新函数
        System.out.println(">:" + id);

        getText(id);
        TextView t = (TextView) findViewById(R.id.r_1);
        t.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if (arg0.getContext().equals("") == false) {
                    const_data.setCur_ID(mNewsList.getList().get(place[0]).getNews_ID());
                    Intent intent = new Intent(ShowNewsActivity.this, ShowNewsActivity.class);// 新建一个界面
                    startActivity(intent);
                }
            }
        });
        TextView t1 = (TextView) findViewById(R.id.r_2);
        t1.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if (arg0.getContext().equals("") == false) {
                    const_data.setCur_ID(mNewsList.getList().get(place[1]).getNews_ID());
                    Intent intent = new Intent(ShowNewsActivity.this, ShowNewsActivity.class);// 新建一个界面
                    startActivity(intent);
                }
            }
        });
        TextView t2 = (TextView) findViewById(R.id.r_3);
        t2.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if (arg0.getContext().equals("") == false) {
                    const_data.setCur_ID(mNewsList.getList().get(place[2]).getNews_ID());
                    Intent intent = new Intent(ShowNewsActivity.this, ShowNewsActivity.class);// 新建一个界面
                    startActivity(intent);
                }
            }
        });


        SharedPreferences sharedPreferences = getSharedPreferences("saved_news_id_list", Context.MODE_PRIVATE); //私有数据
        String tmp = sharedPreferences.getString(id, "");
        if (!tmp.equals("")) {
            issaved = true;
            this.invalidateOptionsMenu();
        }

        init_model();    //设置夜间or日间模式
        if (!const_data.getDay()) {
            toolbar.setBackgroundColor(Color.rgb(66, 66, 66));
            setStatusBarColor(ShowNewsActivity.this, Color.rgb(66, 66, 66));
        }
    }

    public String getDir(int number) {
        String mynumber = String.valueOf(number);

        File f = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + id);
        if (f.exists() == false) {
            f.mkdir();
        }

        return (getExternalFilesDir(null) + File.separator + "Pictures" + File.separator + id + File.separator + mynumber);

    }

    public void setNews(String id) {
        this.id = id;
    }

    private void first_init() {
        setNews(const_data.getCur_ID());
    }

    private void picture_init(String str) {//获取所有图片
        String s = str;
        Pattern p = Pattern.compile("http://([^;\\s])*?\\.(jpg|png|jpeg|gif)");
        Matcher m = p.matcher(s);
        while (m.find())
            picture.add(m.group());

    }

    public void onSuccess(int k) {
        setText(k);
    }

    String clearExtra(String str) {
        String s = str;
        Pattern p = Pattern.compile("\\[[^\\],\\[]*\\]");
        Matcher m = p.matcher(s);
        s = m.replaceAll("");

        p = Pattern.compile("\\|[^\\|]*\\|");
        m = p.matcher(s);
        s = m.replaceAll("");

        p = Pattern.compile("相关新闻");
        m = p.matcher(s);
        s = m.replaceAll("</p><p>");
        return s;
    }

    String setURL(String str, String arm) {//关键字超链接
        String s = str;
        Pattern p = Pattern.compile(arm);
        Matcher m = p.matcher(s);
        s = m.replaceAll("<a href=" + "\"https://baike.baidu.com/item/" + arm + "\"target=\"_blank\">" + arm + "</a>");
        return s;
    }

    String jump_peo(String str) {
        String s = str;
        for (Item2 e : myNews.getPersons()) {
            s = setURL(s, e.word);
        }
        return s;
    }

    String empty_i(int i) {
        String s = "<img src=\"file://" + "/android_res/drawable/t.jpg\"" + "alt=\"" + String.valueOf(i) + "\" " + "/>";

        return s;
    }

    String getPicture_id(int i) {
        String s;
        if (okDownload == true)
            s = "<img src=\"file://" + getDir(i) + gettype(i) + "\"style=\"max-width:100%;\"/>";
        else
            s = "<img src=\"" + picture.get(i) + "\"style=\"max-width:100%;\"/>";
        return s;
    }

    String setbody(String str) {
        String s = str;
        Pattern p = Pattern.compile("\\s{2,1000}");
        Matcher m = p.matcher(s);
        s = m.replaceAll("</p><p>");
        return s;
    }

    String setP(String str, int num) {// 添加图片，图片标号
        String s = str;
        if (num < picture.size()) {
            Pattern p = Pattern.compile(empty_i(num));
            Matcher m = p.matcher(s);
            s = m.replaceAll(getPicture_id(num));
        }
        return s;
    }

    String setEmptyP(String str) {
        String s = str;
        String[] strs = s.split("</p><p>");
        s = "";
        double asd = strs.length * 1.0 / picture.size();
        int k = 0;
        for (int i = 0; (i < strs.length - 1); i++) {
            if (k < picture.size() && k * asd <= i && const_data.getShow_picture() == true) {
                s += strs[i] + "</p>" + empty_i(k) + "<p>";
                k++;
            } else
                s += strs[i] + "</p><p>";
        }
        if (strs[strs.length - 1].contains("下一页") == false
                && strs[strs.length - 1].contains("严禁转载") == false
                )
            s += strs[strs.length - 1];
        return s;
    }

    String gettype(int num) {// 清除多余标签
        String s = picture.get(num);
        Pattern p = Pattern.compile("(jpg|png|jpeg|gif)");
        Matcher m = p.matcher(s);
        m.find();
        return "." + m.group();

    }

    void showText() {
        TextView et2 = (TextView) findViewById(R.id.textView2);
        String ccontext =
                "<html>"
                        + "<body>"
                        + "<h1 align=\"center\">" + title + "</h1>" //标题
                        + "<hr style=\"height:1px;border:none;border-top:1px dashed #555555;\" />"//分割线
                        + "<p>" + context + "</p>"//正文处理
                        + "<hr style=\"height:1px;border:none;border-top:1px dashed #555555;\" />"//分割线
//                        +"<p>"+ context_rec+"</p>"//单页的推荐
                        + "</body>"
                        + "</html>";

        et1.loadDataWithBaseURL("", ccontext, "text/html", "utf-8", null);
        et2.setText("");

    }

    public void setText(int k) {
        int i = k;


        for (int j = k; j < picture.size(); j++) {
            File file = new File(getDir(i) + gettype(i));
            String e = picture.get(j);
            if (const_data.getShow_picture() == true) {
                if (file.exists()) {
                    //图文混排
                    okDownload = true;
                    safepicture.add(e);

                    context = setP(context, safepicture.size() - 1);
                    showText();
                } else {
                    if (picture.size() == 1) {
                        okDownload = false;
                        context = setP(context, i);

                    }
                    showText();
                    getpicture(e, i);//请求图片，请求成功后会再次调用setText进入true
                    return;//避免 图片请求被多次调用
                }
            }
            i++;
        }
        showText();
    }//请求结束后设置显示格式

    public void getpicture(final String url, final int i) {//请求图片
        UrlService service = RetrofitTool.getInstance().getRetrofit().create(UrlService.class);
        Call<ResponseBody> urepos = service.downloadPicFromNet(url);
        urepos.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    writeResponseBodyToDisk(response.body(), i);
                    File file = new File(getDir(i));
                    onSuccess(0);
                } else {
                    okDownload = false;
                    setP(context, i);
                    onSuccess(i + 1);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    public void getText(String id) {//请求文本
        if ((const_data.getHaveRead().contains(id) && const_data.getHaveReadNewsById(id) != null)) {
            News data;
            data = const_data.getHaveReadNewsById(id);
            String myt = data.getNews_Title() + "," + data.getNews_Content();
            speaker.setText(myt);
            news_content = myt;
            picture_init(data.getNews_Pictures());
            System.out.println(data.getNews_Pictures());
            title = data.getNews_Title();
            context = data.getNews_Content();
            context = "</p><p>" + context;
            myNews = data;
            System.out.println(picture.size() + "here");
            if (picture.size() > minnumber) {
                context = clearExtra(context);
                context = jump_peo(context);
                context = setbody(context);
                context = setEmptyP(context);
                showText();
                setText(0);
            } else {
                if (const_data.getShow_picture() == true) {
                    okDownload = false;
                    MyTask task = new MyTask();
                    task.execute(title);
                } else {
                    context = clearExtra(context);
                    context = jump_peo(context);
                    context = setbody(context);
                    context = setEmptyP(context);
                    showText();
                    setText(0);
                }
            }
            if (const_data.isConnect())
                getContext_rec();//请求关键词
        } else if (const_data.getIsDownload().contains(id)) {
            News data = const_data.getCur_news();
            String myt = data.getNews_Title() + "," + data.getNews_Content();
            speaker.setText(myt);
            news_content = myt;
            picture_init(data.getNews_Pictures());
            System.out.println(data.getNews_Pictures());
            title = data.getNews_Title();
            context = data.getNews_Content();
            context = "</p><p>" + context;
            myNews = data;
            System.out.println(picture.size() + "here");
            if (picture.size() > minnumber) {
                context = clearExtra(context);
                context = jump_peo(context);
                context = setbody(context);
                context = setEmptyP(context);
                showText();
                setText(0);
            } else {
                if (const_data.getShow_picture() == true) {
                    okDownload = false;
                    MyTask task = new MyTask();
                    task.execute(title);
                } else {
                    context = clearExtra(context);
                    context = jump_peo(context);
                    context = setbody(context);
                    context = setEmptyP(context);
                    showText();
                    setText(0);
                }
            }
            if (const_data.isConnect())
                getContext_rec();//请求关键词
        } else {
            NewsService service = retrofitTool.getRetrofit().create(NewsService.class);

            Call<News> repos = service.listRepos(id);

            repos.enqueue(new Callback<News>() {
                @Override
                public void onResponse(Call<News> call, Response<News> response) {

                    if (response.isSuccessful()) {
                        News data = new News();
                        data = response.body();
                        if (data != null) {
                            const_data.addHaveRead(data);
                            String myt = data.getNews_Title() + "," + data.getNews_Content();
                            speaker.setText(myt);
                            news_content = myt;
                            List<Item1> a = data.getKeywords();
                            int j = 10;
                            for (Item1 i : a) {//添加关键词
                                const_data.addLike(i.word, Double.valueOf(i.score).intValue());
                                if (--j == 0)
                                    break;
                            }


                            picture_init(data.getNews_Pictures());
                            title = data.getNews_Title();
                            context = data.getNews_Content();
                            context = "</p><p>" + context;

                            myNews = data;

                            if (picture.size() > minnumber) {
                                context = clearExtra(context);
                                context = jump_peo(context);
                                context = setbody(context);
                                context = setEmptyP(context);
                                showText();
                                setText(0);
                            } else {
                                okDownload = false;
                                if (const_data.getShow_picture() == true) {
                                    MyTask task = new MyTask();
                                    task.execute(title);
                                } else {
                                    context = clearExtra(context);
                                    context = jump_peo(context);
                                    context = setbody(context);
                                    context = setEmptyP(context);
                                    showText();
                                    setText(0);

                                }
                            }
                            if (const_data.isConnect())
                                getContext_rec();//请求关键词
                        }
                    } else {
                    }
                }

                @Override
                public void onFailure(Call<News> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    private boolean writeResponseBodyToDisk(ResponseBody body, int i) {//图片缓存-写到文件里

        try {
            File futureStudioIconFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + id);
            if (futureStudioIconFile.exists() == false) {
                futureStudioIconFile.mkdir();
            }

            futureStudioIconFile = new File(getDir(i) + gettype(i));
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                }
                outputStream.flush();
                return true;

            } catch (Exception e) {

                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_shownews, menu);
        item_voice = menu.findItem(R.id.voice);
        item_stop = menu.findItem(R.id.stop);
        item_saved = menu.findItem(R.id.save);
        item_cancel_saved = menu.findItem(R.id.cancel_save);
//        item_download = menu.findItem(R.id.download);
//        item_delete = menu.findItem(R.id.delete);
        if (isread) {
            item_voice.setVisible(false);
            item_stop.setVisible(true);
        } else {
            item_stop.setVisible(false);
            item_voice.setVisible(true);
        }
        if (const_data.getIsDownload().contains(id) || issaved) {
            item_saved.setVisible(false);
            item_cancel_saved.setVisible(true);
        } else {
            item_saved.setVisible(true);
            item_cancel_saved.setVisible(false);
        }
        /*if ( const_data.getIsDownload().contains(id) )
        {
            item_download.setVisible(false);
            item_delete.setVisible(true);
        }
        else
        {
            item_delete.setVisible(false);
            item_download.setVisible(true);
        }*/
        return super.onCreateOptionsMenu(menu);
    }

    public void shareMsg(String activityTitle, String msgTitle, String msgText, String imgPath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (imgPath == null || imgPath.equals("")) {
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
            intent.putExtra(Intent.EXTRA_TEXT, msgText);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Intent.createChooser(intent, activityTitle));
        } else {
            File f = new File(imgPath);
            if (f != null && f.exists() && f.isFile()) {
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, msgText);
                intent.setType("image/*");
                Uri u = Uri.fromFile(f);
                intent.putExtra(Intent.EXTRA_STREAM, u);

                intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, activityTitle));
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                News aa = getNews();
                shared_content = aa.getNews_Title() + "\r\n" + aa.getNews_URL();
                shareMsg("分享图片和文字", "Share", shared_content, null);
                return true;
            case R.id.save:
                save_news();
                break;
            case R.id.cancel_save:
                isdownload = false;
                del_news();
                break;
            /*case R.id.download:
                download();
                break;
            case R.id.delete:
                deleteFile(id+".txt");
                isdownload = false;
                const_data.removeDownload(id);
                this.invalidateOptionsMenu();
                Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
                break;*/
            case R.id.voice:
                voice_begin();
                break;
            case R.id.stop:
                voice_stop();
                break;
            default:
                Toast.makeText(this, "方法还没定义", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }

    public void download() {
        isdownload = true;
        const_data.addDownload(id);
        this.invalidateOptionsMenu();
        FileHelper fHelper = new FileHelper(mContext);
        try {
            //保存文件名和内容
            fHelper.save(id + ".txt", news_content);
            Toast.makeText(getApplicationContext(), "下载成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            //写入异常时
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_SHORT).show();
        }
        File file = new File(Environment.getExternalStorageDirectory(),
                id + ".txt");
    }

    public void save_news() {
        issaved = true;
        this.invalidateOptionsMenu();
        if (const_data.getIsDownload().size() >= 20)
            Toast.makeText(getApplicationContext(), "您最多只可收藏20条新闻……", Toast.LENGTH_SHORT).show();
//        const_data.addDownload(id);

        FileHelper fHelper = new FileHelper(mContext);
        try {
            fHelper.save(id + ".txt", news_content);
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        File file = new File(Environment.getExternalStorageDirectory(),
                id + ".txt");
        News SavedNews = getNews();
        try {

            FileOutputStream fos = mContext.openFileOutput(id + ".txt", Context.MODE_PRIVATE);
            System.out.println("show news Save + :" + SavedNews.getNews_ID());

            ObjectOutputStream oos = new ObjectOutputStream(fos);
            News news = const_data.getCur_news();
            SavedNews.setNews_Intro(news.getNews_Intro());
            SavedNews.setNews_Source(news.getNews_Source());
            SavedNews.setNews_Author(news.getNews_Author());
            oos.writeObject(SavedNews);
            System.out.println("wo cao ni ma ");
            oos.flush();
            oos.close();
            fos.close();

//            Toast.makeText(getApplicationContext(), "收藏成功", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            System.out.println("File lost");
            e.printStackTrace();
        }

        SharedPreferences sharedPreferences = getSharedPreferences("saved_news_id_list", Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putString(id, id);
        editor.commit();//提交修改

        SharedPreferences shared = getSharedPreferences("saved_news_num", Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor e = shared.edit();//获取编辑器
        int num = shared.getInt("num", 0);
        num++;
        e.putInt("num", num);
        e.commit();//提交修改
        isdownload = true;
        const_data.addDownload(id);

        Toast.makeText(getApplicationContext(), "收藏成功", Toast.LENGTH_SHORT).show();
    }

    public void del_news() {
        issaved = false;
        deleteFile(id + ".txt");
        const_data.removeDownload(id);
        this.invalidateOptionsMenu();
        SharedPreferences sharedPreferences = getSharedPreferences("saved_news_id_list", Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putString(id, "");
        editor.commit();//提交修改

        SharedPreferences shared = getSharedPreferences("saved_news_num", Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor e = shared.edit();//获取编辑器
        int num = shared.getInt("num", 0);
        num--;
        if (num < 0) num = 0;
        e.putInt("num", num);
        e.commit();
        Toast.makeText(getApplicationContext(), "取消收藏", Toast.LENGTH_SHORT).show();

    }

    public void voice_begin() {
        speaker.setCur(this);
        speaker.start();
        isread = true;
        this.invalidateOptionsMenu();
    }

    public void voice_stop() {
        speaker.stop();
        isread = false;
        this.invalidateOptionsMenu();
    }

    public void init_model() {
        if (!isDay) {
            ThemeManager.setThemeMode(ThemeManager.ThemeMode.DAY);
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
        et1.setBackgroundColor(getResources().getColor(ThemeManager.getCurrentThemeRes(ShowNewsActivity.this, R.color.naviColor)));
        linearLayout.setBackgroundColor(getResources().getColor(ThemeManager.getCurrentThemeRes(ShowNewsActivity.this, R.color.naviColor)));
        toolbar.setTitleTextColor(getResources().getColor(ThemeManager.getCurrentThemeRes(ShowNewsActivity.this, R.color.titleColor)));
        toolbar.setBackgroundColor(getResources().getColor(ThemeManager.getCurrentThemeRes(ShowNewsActivity.this, R.color.toolColor)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ThemeManager.unregisterThemeChangeListener(this);
    }
}
