package com.bignewsmaker.makebignews.extra_class;

import java.util.*;

/**
 * Created by liminyan on 07/09/2017.
 */


class Item2
{
    String word;
    String count;
}

public class MyNews {

   public ArrayList<Item1> Keywords;
//    List<item> bagOfWords;
//    String crawl_Source;
//    String crawl_Time;
//    String inborn_KeyWords;
//    String lang_Type;
//    List<item2> locations;
//    String newsClassTag;
//    String news_Author;
//    String news_Category;
   public String news_Content;
//    String news_ID;
//    String news_Journal;
//    String news_Pictures;
//    String news_Source;
//    String news_Time;
   public String news_Title = "1";
//    String news_URL;
//    String news_Video;
//    List<item2> organizations;
//    List<item2> persons;
//    String repeat_ID;
////    String seggedPListOfContent;
//    String wordCountOfContent;
//    String  wordCountOfTitle;


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




















