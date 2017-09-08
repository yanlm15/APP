package com.bignewsmaker.makebignews.Functiontool;

import com.bignewsmaker.makebignews.LIST;
import com.bignewsmaker.makebignews.News;

import java.util.TreeMap;

/**
 * Created by liminyan on 06/09/2017.
 * 这个类的作用是储存全局的设置变量
 * 其他的activity 可以访问这个类
 * 通过set 来设置
 * 通过get 来访问
 * setSearch_message 设置搜索信息
 * getSearch_message 获取搜索信息
 */

public class ConstData {
    private double ligth_rate = 1; //屏幕亮度比例
    private boolean show_picture = true; //图片显示标签
    private  boolean model_night = true;
    private News cur_news = null ;// 当前新闻列表
    private LIST cur_list = null ;// 当前选中新闻
    private News search_result = null ;// 当前的搜索结果
    private static ConstData cur;

    private TreeMap<String,Integer> like = new TreeMap<String,Integer>(); // 喜欢词条，添加历史记录
    private String cur_ID = "201608090432c815a85453c34d8ca43a591258701e9b";

    private TreeMap<String,Integer> dislike = new TreeMap<String,Integer>();// 不喜欢的词条

    private String  search_message = null;//用户的输入信息
    private String  search_class = null;//用户的搜索标签，默认为空！！
    private String cur_pageSize = "20";

    public void setSearch_class(String search_class) {
        this.search_class = search_class;
    }

    public void setModel_night(boolean model_night) {
        this.model_night = model_night;
    }

    public boolean isModel_night() {
        return model_night;
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

    public static ConstData getCur() {
        return cur;
    }

    public void setSearch_message(String search_message) {
        this.search_message = search_message;
    }//初始化搜索信息

    public void init()
    {
        double ligth_rate = 1;
        boolean show_picture = true;
    }

    public void setSearch_result(News search_result) {
        this.search_result = search_result;
    }//初始化搜索结果

    public TreeMap<String, Integer> getDislike() {
        return dislike;
    }

    public TreeMap<String, Integer> getLike() {
        return like;
    }

    public void setCur_ID(String cur_ID) {
        this.cur_ID = cur_ID;
    }

    public void setDislike(String name) {
        this.dislike.put(name,1);
    }

    public void setLike(String name) {
        boolean e = this.like.containsKey(name);
        if (e == true)
        {
            String s_name = name;
            int number = this.like.get(s_name);
            number ++;
            this.like.put(s_name,number);
        }
    }

    public void setCur_news(News cur_news) {
        this.cur_news = cur_news;
    }//初始新闻列表

    public void setCur_list(LIST cur_list) {
        this.cur_list = cur_list;
    }//初始化选中新闻

    public void setLigth_rate(double ligth_rate) {
        this.ligth_rate = ligth_rate;
    }//初始化亮度参数

    public void setShow_picture(boolean show_picture) {
        this.show_picture = show_picture;
    }//初始化图片显示标签

    public News getSearch_result() {
        return search_result;
    }

    public News getCur_news() {return cur_news;}

    public LIST getCur_list() {return cur_list;}

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
