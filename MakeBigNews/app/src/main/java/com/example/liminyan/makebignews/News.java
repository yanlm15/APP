package com.example.liminyan.myapplication;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by liminyan on 01/09/2017.
 */

class News {
    String label;
    String text;
    Date d;
    //...
    //...
    boolean show_image;
}

class NewsManager{
    ArrayList<News> mynews = new  ArrayList<News>();
    ArrayList<News> favonews = new  ArrayList<News>();

    ArrayList<News> init(String str)
    {
        ArrayList<News> m = new  ArrayList<News>();




        return  m;
    }

    ArrayList<News> find_list(String str)
    {
        ArrayList<News> m = new  ArrayList<News>();




        return  m;
    }

    News find_news(String str)
    {
        News ans = new News();

        return  ans;
    }

    void load()
    {

    }

    void dump()
    {

    }
    //...;

}


