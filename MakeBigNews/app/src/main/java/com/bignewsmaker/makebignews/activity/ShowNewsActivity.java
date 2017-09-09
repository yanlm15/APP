package com.bignewsmaker.makebignews.activity;

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

import com.bignewsmaker.makebignews.Interface.SuccessCallBack;
import com.bignewsmaker.makebignews.basic_class.ConstData;
import com.bignewsmaker.makebignews.basic_class.Item2;
import com.bignewsmaker.makebignews.extra_class.RetrofitTool;
import com.bignewsmaker.makebignews.R;
import com.bignewsmaker.makebignews.extra_class.Speaker;
import com.bignewsmaker.makebignews.extra_class.ThemeManager;
import com.bignewsmaker.makebignews.basic_class.Item1;
import com.bignewsmaker.makebignews.basic_class.MyNews;
import com.bignewsmaker.makebignews.Interface.NewService;
import com.bignewsmaker.makebignews.Interface.UrlService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
 *
 *
 * 图片获取方法 处理函数通过getpicture()请求，成功后再回调处理函数
 * 其中处理函数 需要添加判断是否本地有存储 如果有则倒入本地资源
 * 类似的思想可以应用到离线离线新闻列表中
 * 每次打开app就判读 a这个文件是否为空，是调用网络响应反馈加载，否则将文本a的数据写到界面，
 * 触发更新函数后就等待网络响应，获取新的新闻，关闭然后将最后一次获取的新闻写到a
 * 以上就是离线列表的实现思路
 *
 * 设置信息储存，当然要是实现永久性，将信息写到一个文件里，然后开机的时候倒入
 * getExternalFilesDir(null)获取外部存储目录
 *
 */



public class ShowNewsActivity extends AppCompatActivity implements ThemeManager.OnThemeChangeListener,SuccessCallBack {

    private ConstData const_data = ConstData.getInstance();// 设置访问全局变量接口
    private Speaker speaker = Speaker.getInstance();// 设置语音系统接口
    private RetrofitTool retrofitTool = RetrofitTool.getInstance();
    private String id;
    private String title;
    private String context;
    private MyNews myNews = new MyNews();
    private ArrayList<String> picture = new ArrayList<String >();
    private ArrayList<Bitmap> mybitmap = new ArrayList<Bitmap>();

    private boolean isDay = const_data.isModel_day();
    private LinearLayout linearLayout;
    private Toolbar toolbar;
    private TextView textView, textView2;
    private MenuItem item_voice, item_stop, item_day, item_night;
    private boolean isread = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        first_init(); // 获取当前新闻信息
        //设置输入监控
        //设置更新函数

        getText(id);
        init_model();    //设置夜间or日间模式
    }

    public String  getDir(int number)
    {
        String mynumber = String.valueOf(number);
        return (getExternalFilesDir(null) + File.separator+"picture"+File.separator+id+File.separator+mynumber );

    }

    public void setNews(String id) {
        this.id = id;
    }

    private void first_init()
    {
        setNews(const_data.getCur_ID());
    }

    private void picture_init(String str){//获取所有图片
        String s = str;
        Pattern p = Pattern.compile("http://(\\w+|\\/|\\.|-)*.(jpg|png|jpeg)");
        Matcher m = p.matcher(s);

        while( m.find())
            picture.add(m.group());
        for (String e : picture) {
             System.out.println(e);

        }

    }
    public void onSuccess(String str){
        setText();
    }

    String setURL(String str,String arm) //关键字超链接
    {
        String s = str;
        Pattern p = Pattern.compile(arm);
        Matcher m = p.matcher(s);
//
        s=m.replaceAll("<a href="+"\"https://baike.baidu.com/item/"+arm+"\"target=\"_blank\">"+arm+"</a>");
//        https://baike.baidu.com/item/+"arm"
//        <a href="http://www.w3school.com.cn/" target="_blank">Visit W3School!</a>
        return s;
    }

    String jump_peo (String str)
    {
        String s = str;
        for (Item2 e : myNews.getPersons() )
        {
            s = setURL(s,e.word);
        }
        return  s;
    }

//    String jump_pla(String str)//地点
//    {
//        String s = str;
//        for (Item2 e : myNews.getPlace() )
//        {
//            s = setURL(s,e.word);
//        }
//        return  s;
//    }
    String getPicture_id(int i)
    {
        String s= "<p style=\"text-align:center\"><img src=\""+getDir(i)+"\"></p>";
//        <p style="text-align:center"><img src="/i/eg_tulip.jpg"></p>
        return s;
    }

    String setP(String str,int num)// 分段 添加图片占位符号 待修改的文本，参数为添加图片数目
    {
        String s=str;
        Pattern p = Pattern.compile("\\s{2,1000}");
//        String arm = "俄罗斯";
        Matcher m = p.matcher(s);

        int textnumber = m.end();
        s=m.replaceAll("</p><p>");
        if (num > 0)
        {
            String[] strs = s.split("</p><p>");
            s = "";
            s+=getPicture_id(0);
            for (int i=1 ; (i < strs.length-1) && (i<num) ;i++)
            {
                s+=strs[i]+"</p>"+getPicture_id(i)+"<p>";
            }
        }

        return s;
    }


    String clearExtra(String s)// 清除多余标签
    {
        return "s";
    }
    public void setText(){
        int i = 0;
        for (String e:picture)
        {
            System.out.println("><:"+e);//用于测试输出

            File file =  new File(getDir(i));

            if (const_data.getShow_picture() == true){
                if (file.exists()) {
                    //图文混排
                    System.out.println(">>:>"+e+"\n"+getDir(i));
                    Bitmap p =  BitmapFactory.decodeFile(getDir(i));
                    mybitmap.add(p);
                    break;//暂时添加一张图片
                }
                else {
                    System.out.println(">:"+e);
                    getpicture(e,i);//请求图片，请求成功后会再次调用setText进入true
                    return;//避免 图片请求被多次调用
                }
            }

            i++;
        }
        TextView et1 = (TextView) findViewById(R.id.textView);//content
        if (mybitmap.size()>0) {
            //  如果有图片
            System.out.println("you get it");
            context = jump_peo(context);
            context = setP(context,mybitmap.size());
        }
        else {
            System.out.println("no picture");
            context = jump_peo(context);
            context = setP(context,0);
        }

    String ccontext =
            "<html>"
                +"<body>"
                    +"<h1 align=\"center\">"+title+ "</h1>" //标题
                    +"<hr style=\"height:1px;border:none;border-top:1px dashed #555555;\" />"//分割线
                    + "<img src="+getDir(0)+ "style=\"max-width:100%;\"/>"//测试第一张（没有会暂时报错）
                    +"<p>"+ context+"</p>"//正文处理
                +"</body>"
            +"</html>";
        et1.setText("\n"+ccontext+"\n");
    }

    public void getpicture(final String url,final int i)
    {
        UrlService service = RetrofitTool.getInstance().getRetrofit().create(UrlService.class);
        System.out.println(">>><<<");

        Call<ResponseBody> urepos = service.downloadPicFromNet(url);

        urepos.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful())
                {
                    System.out.println(">>>"+url);
                    writeResponseBodyToDisk(response.body(), i);
                    File file = new File(getDir(i));
                    System.out.println(">>:"+getDir(i));
                    System.out.println(file.exists());

                    System.out.println("Url-Success");
                    onSuccess(">>");
                }else {
                    System.out.println("Url-err");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("Url-f-err"+t.toString());
            }
        });

        System.out.println(">>>|<<<");
    }

    public void getText(String id)
    {

        NewService service = retrofitTool.getRetrofit().create(NewService.class);

        Call<MyNews> repos = service.listRepos(id);

        repos.enqueue(new Callback<MyNews>() {
            @Override
            public void onResponse(Call<MyNews> call, Response<MyNews> response) {

                if (response.isSuccessful()) {
                    System.out.println("news-success");
                    MyNews data = new MyNews();
                    data = response.body();
                    if (data != null) {
                        String myt = data.getNews_Title() + "," + data.getNews_Content();
                        speaker.setText(myt);
                        ArrayList<Item1> a = data.getKeywords();
                        for (Item1 i : a){//添加关键词
                            const_data.setLike(i.word);
//                            System.out.println(i.word);//用于测试输出
                        }

                        picture_init(data.getNews_Pictures());
                        System.out.println(data.getNews_Pictures());

                        title =  data.getNews_Title();
                        context = data.getNews_Content();
                        myNews = data;
                        setText();

                    }
                } else {
                    System.out.println("fuck");
                }

            }

            @Override
            public void onFailure(Call<MyNews> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }



    private boolean writeResponseBodyToDisk(ResponseBody body,int i)
    {

        try{
        File futureStudioIconFile = new File(getExternalFilesDir(null) + File.separator+"picture"+File.separator+id);
            if (futureStudioIconFile.exists()==false)
            {
                futureStudioIconFile.mkdir();
            }
        futureStudioIconFile = new File(getDir(i));
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
            System.out.println("TR");
            return true;

        }catch (Exception e)
        {
            System.out.println("FTR"+e);

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
