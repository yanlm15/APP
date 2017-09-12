package com.bignewsmaker.makebignews.extra_class;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liminyan on 12/09/2017.
 * 指定关键词获取一张网络图片
 * 例子
 * http://image.baidu.com/search/index?tn=baiduimage&ps=1&ct=201326592&lm=-1&cl=2&nc=1&ie=utf-8&word=好人
 */

public class InternetPicturetool {

    private  final String url = "http://image.baidu.com/search/index?tn=baiduimage&ps=1&ct=201326592&lm=-1&cl=2&nc=1&ie=utf-8&word=";
    private  final String ECODING = "UTF-8";
    private  final String IMGURL_REG = "<img.*src=(.*?)[^>]*?>";
    private  final String IMGSRC_REG = "http:\"?(.*?)(\"|>|\\s+)";
    private  String arm="";

    static InternetPicturetool cur=null;

    public static InternetPicturetool getInstance() {
        if (cur == null)
        {
            cur = new InternetPicturetool();
        }
        return cur;
    }

    void getNewPicture(String str) //获取补全网络图片
    {
        this.arm =url+str;

    }

    private String getHTML(String url) throws Exception { //这里为了方便处理
        URL uri = new URL(url);
        URLConnection connection = uri.openConnection();
        InputStream in = connection.getInputStream();
        byte[] buf = new byte[1024];
        int length = 0;
        StringBuffer sb = new StringBuffer();
        while ((length = in.read(buf, 0, buf.length)) > 0) {
            sb.append(new String(buf, ECODING));
//            break;
        }
        in.close();
        return sb.toString();
    }

    private ArrayList<String> getImageUrl(String HTML) {
        Matcher matcher = Pattern.compile(IMGURL_REG).matcher(HTML);
        ArrayList<String> listImgUrl = new ArrayList<String>();
        while (matcher.find()) {
            listImgUrl.add(matcher.group());
        }
        return listImgUrl;
    }

    private ArrayList<String> getImageSrc(ArrayList<String> listImageUrl) {
        ArrayList<String> listImgSrc = new ArrayList<String>();
        for (String image : listImageUrl) {
            Matcher matcher = Pattern.compile(IMGSRC_REG).matcher(image);
            while (matcher.find()) {
                listImgSrc.add(matcher.group().substring(0, matcher.group().length() - 1));
            }
        }
        return listImgSrc;
    }


}
