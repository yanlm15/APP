package com.bignewsmaker.makebignews.basic_class;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by liminyan on 06/09/2017.
 * 这个类的作用是储存全局的设置变量
 * 其他的activity 可以访问这个类
 * 通过set 来设置
 * 通过get 来访问
 * setSearch_message 设置搜索信息
 * getSearch_message 获取搜索信息
 * like重载了比较函数实现了根据value进行排序
 *
 */

public class ConstData {

    private ConstData(){
        show_picture = true;
        isDay = true;
        cur_pageSize="20";
        isFirstCreate = true;
    }
    private double ligth_rate = 1; //屏幕亮度比例
    private boolean show_picture; //图片显示标签
    private boolean isDay;
    private boolean connect=true;

    public void setConnect(boolean connect) {
        this.connect = connect;
    }

    public boolean isConnect() {
        return connect;
    }

    private boolean isSetChanged =false;
    private boolean safe_search = false;
    private NewsList cur_newslist = null ;// 当前新闻列表
    private News cur_news = null ;// 当前选中新闻
    private NewsList search_result = null ;// 当前的搜索结果

    private HashSet<String> filtered=new HashSet<>();
    private static ConstData cur;
    private Map<String,Integer> like = new TreeMap<String, Integer>();

    private String cur_ID = "201608090432c815a85453c34d8ca43a591258701e9b";

    private HashSet<String> dislike = new HashSet<>();// 不喜欢的词条

    private String  search_message = null;//用户的输入信息
    private String  search_class = null;//用户的搜索标签，默认为空！！
    private String cur_pageSize ;

    public static final String TAG = "makebignews";
    private HashMap<String,News> haveRead=new HashMap<>();
    private boolean[] istagSelected={true,true,true,true,true,true,true,true,true,true,true,true,true,true};

    private boolean isFirstCreate;
    private boolean isExit;
    private Set<String> isDownload=new HashSet<>();

    public void removeDownload(String id){
        isDownload.remove(id);
    }

    public void addDownload(String id){
        isDownload.add(id);
    }

    public Set<String> getIsDownload() {
        return isDownload;
    }

    public void setIsDownload(Set<String> isDownload) {
        this.isDownload = isDownload;
    }

    public boolean isExit() {
        return isExit;
    }

    public void setExit(boolean exit) {
        isExit = exit;
    }

    public boolean isFirstCreate() {
        return isFirstCreate;
    }

    public void setFirstCreate(boolean firstCreate) {
        isFirstCreate = firstCreate;
    }

    public void setFiltered(HashSet<String> filtered) {
        this.filtered = filtered;
    }

    public HashSet<String> getFiltered() {
        return filtered;
    }

    public void addFiltered(String filtering) {
        filtered.add(filtering);
    }
    public boolean getIstagSelected(int index) {
        return istagSelected[index];
    }

    public void setIstagSelected(int index, boolean isSelected) {
        istagSelected[index] = isSelected;
    }
    public int getTagSize(){
        int a=0;
        for(boolean e:istagSelected)
            if(e)
                a++;
        return a;
    }

    public void setSafe_search(boolean safe_search) {this.safe_search = safe_search;}

    public boolean isSafe_search() {return safe_search;}

    public News getHaveReadNewsById(String id) {
        return haveRead.get(id);
    }
    public Set<String> getHaveRead(){
        return haveRead.keySet();
    }

    public Set<String> getLikeWord(){
        return like.keySet();
    }

    public void addHaveRead(News news) {
        haveRead.put(news.getNews_ID(),news);
    }

    public void setHaveRead(HashMap<String, News> haveRead) {
        this.haveRead = haveRead;
    }

    public boolean isSetChanged() {
        return isSetChanged;
    }

    public void setSetChanged(boolean setChanged) {
        isSetChanged = setChanged;
    }

    public boolean getDay() {
        return isDay;
    }

    public void setDay(boolean day) {
        isDay = day;
    }

    public void setSearch_class(String search_class) {
        this.search_class = search_class;
    }

    public void setModel_day(boolean isDay) {
        this.isDay = isDay;
    }

    public boolean isModel_day() {
        return isDay;
    }

    public String getSearch_class() {
        return search_class;
    }

    public String getCur_pageSize() {
        return cur_pageSize;
    }

    public void setCur_pageSize(String cur_pageSize) {
        this.cur_pageSize = cur_pageSize;
    }

    public String getSearch_message() {
        return search_message;
    }

    public String getCur_ID() {
        return cur_ID;
    }

    public void setSearch_message(String search_message) {
        this.search_message = search_message;
    }//初始化搜索信息

    public void init()
    {
        ligth_rate = 1;
        show_picture = true;
    }

    public void setSearch_result(NewsList search_result) {
        this.search_result = search_result;
    }//初始化搜索结果

    public HashSet<String>  getDislike() {
        return dislike;
    }

    public void setLike(Map<String, Integer> like) {
        this.like = like;
    }

    public Map<String, Integer> getLike() {
        return like;
    }

    public void setCur_ID(String cur_ID) {
        this.cur_ID = cur_ID;
    }

    public void addDislike(String name) {
        this.dislike.add(name);
    }

    public void setDislike(HashSet<String> dislike) {
        this.dislike = dislike;
    }

    public void addLike(String name,Integer value) {
        like.put(name,like.containsKey(name)?like.get(name)+value:value);
    }



    public void setCur_newslist(NewsList cur_newslist) {
        this.cur_newslist = cur_newslist;
    }//初始新闻列表

    public void setCur_news(News cur_news) {
        this.cur_news = cur_news;
    }//初始化选中新闻

    public void setLigth_rate(double ligth_rate) {
        this.ligth_rate = ligth_rate;
    }//初始化亮度参数

    public void setShow_picture(boolean show_picture) {
        this.show_picture = show_picture;
    }//初始化图片显示标签

    public NewsList getSearch_result() {
        return search_result;
    }

    public NewsList getCur_newslist() {return cur_newslist;}

    public News getCur_news() {return cur_news;}

    public double getLigth_rate() {
        return ligth_rate;
    }

    public boolean getShow_picture()
    {
        return show_picture;
    }

    public static ConstData getInstance() {

        if (cur == null)
            cur = new ConstData();
        return cur;
    }
}
