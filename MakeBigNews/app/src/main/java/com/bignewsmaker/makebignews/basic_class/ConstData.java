package com.bignewsmaker.makebignews.basic_class;


import java.util.Comparator;
import java.util.HashSet;
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
    private double ligth_rate = 1; //屏幕亮度比例
    private boolean show_picture = true; //图片显示标签
    private boolean isDay = true;
    private boolean isSetChanged =false;
    private boolean safe_search = false;
    private NewsList cur_news = null ;// 当前新闻列表
    private News cur_list = null ;// 当前选中新闻
    private NewsList search_result = null ;// 当前的搜索结果

    public HashSet<String> getFiltered() {
        return filtered;
    }

    public void addFiltered(String filtering) {
       filtered.add(filtering);
    }

    private HashSet<String> filtered=new HashSet<>();
    private static ConstData cur;
    private TreeMap<String,Integer> like1 = new TreeMap<String, Integer>();
    private TreeMap<String,Integer> like = new TreeMap<String, Integer>(new Comparator<String>() {
        @Override
        public int compare(String s, String t1) {
                if (like1.get(s) < like1.get(t1))
                    return 1;
                if (like1.get(s) > like1.get(t1))
                    return -1;
            return 0;
        }
    });

    private String cur_ID = "201608090432c815a85453c34d8ca43a591258701e9b";

    private TreeMap<String,Integer> dislike = new TreeMap<String,Integer>();// 不喜欢的词条

    private String  search_message = null;//用户的输入信息
    private String  search_class = null;//用户的搜索标签，默认为空！！
    private String cur_pageSize = "20";

    public static final String TAG = "makebignews";
    private HashSet<String> haveRead=new HashSet<String>();
    private boolean[] istagSelected={true,true,true,true,true,true,true,true,true,true,true,true,true};

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

    public HashSet<String> getHaveRead() {
        return haveRead;
    }

    public void addHaveRead(String id) {
        haveRead.add(id);
    }

    public TreeMap<String, Integer> getLike1() {
        return like1;
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
        double ligth_rate = 1;
        boolean show_picture = true;
    }

    public void setSearch_result(NewsList search_result) {
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
        }else {
            this.like.put(name,0);
        }
    }

    public void setLike1(String  name) {

        boolean e = this.like1.containsKey(name);
        if (e == true)
        {
            String s_name = name;
            int number = this.like1.get(s_name);
            number ++;
            this.like1.put(s_name,number);
        }else {
            this.like1.put(name,0);
        }
    }

    public void setCur_news(NewsList cur_news) {
        this.cur_news = cur_news;
    }//初始新闻列表

    public void setCur_list(News cur_list) {
        this.cur_list = cur_list;
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

    public NewsList getCur_news() {return cur_news;}

    public News getCur_list() {return cur_list;}

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
