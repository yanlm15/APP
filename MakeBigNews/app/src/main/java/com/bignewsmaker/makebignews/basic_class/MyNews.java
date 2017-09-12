package com.bignewsmaker.makebignews.basic_class;

import java.util.*;

import static java.lang.Math.min;

/**
 * Created by liminyan on 07/09/2017.
 */



public class MyNews{

   public ArrayList<Item1> Keywords;
//    List<item> bagOfWords;
//    String crawl_Source;
//    String crawl_Time;
//    String inborn_KeyWords;
    public String lang_Type;
//    List<item2> locations;
    String newsClassTag;
   public   String news_Author;
//    String news_Category;
   public String news_Content;
    String news_ID;
//    String news_Journal;
   public String news_Pictures;
   public String news_Source;
   public String news_Time;
   public String news_Title = "1";
    String news_URL;
    String news_Video;
//    List<item2> organizations;
    public ArrayList<Item2> persons;
    public String repeat_ID;
    public ArrayList<String > seggedPListOfContent;
    public String seggedTitle;
//    String wordCountOfContent;
//    String  wordCountOfTitle;


    public News formNews(MyNews a)
    {
        News b = new News();
        b.setLang_Type(a.lang_Type);
        b.setNews_Author(a.news_Author);
        b.setNews_ID(a.news_ID);
        b.setNews_Intro(a.news_Content.substring(0,min(20,news_Content.length()))+"...");
        b.setNews_Pictures(a.news_Pictures);
        b.setNews_Source(a.news_Source);
        b.setNews_Time(a.news_Time);
        b.setNews_Title(a.news_Title);
        b.setNews_URL(a.news_URL);
        b.setNews_Video(a.news_Video);
        b.setNewsClassTag(a.newsClassTag);
        b.setRead(true);
        return  b;
    }

    public ArrayList<String> getSeggedPListOfContent() {
        return seggedPListOfContent;
    }

    public void setSeggedPListOfContent(ArrayList<String> seggedPListOfContent) {
        this.seggedPListOfContent = seggedPListOfContent;
    }

    public void setRepeat_ID(String repeat_ID) {this.repeat_ID = repeat_ID;}

    public String getRepeat_ID() {return repeat_ID;}

    public void setPersons(ArrayList<Item2> persons) {
        this.persons = persons;
    }

    public ArrayList<Item2> getPersons() {
        return persons;
    }

    public String getNews_Time() {
        return news_Time;
    }

    public void setNews_Time(String news_Time) {
        this.news_Time = news_Time;
    }

    public String getSeggedTitle() {
        return seggedTitle;
    }

    public void setSeggedTitle(String seggedTitle) {
        this.seggedTitle = seggedTitle;
    }

    public String getNews_Pictures() {
        return news_Pictures;
    }

    public void setNews_Pictures(String news_Pictures) {
        this.news_Pictures = news_Pictures;
    }

    public ArrayList<Item1> getKeywords() {
        return Keywords;
    }

    public void setKeywords(ArrayList<Item1> keywords) {
        this.Keywords = keywords;
    }

    public String getNews_Content() {
        return news_Content;
    }

    public void setNews_Content(String news_Content) {
        this.news_Content = news_Content;
    }

    public String getNews_Title() {
        return news_Title;
    }

    public void setNews_Title(String news_Title) {
        this.news_Title = news_Title;
    }

}




