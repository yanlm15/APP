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
//    private  final String url = "https://images.search.yahoo.com/search/images;_ylt=AwrTcXnNQrhZoCsA2rqJzbkF;_ylu=X3oDMTBsZ29xY3ZzBHNlYwNzZWFyY2gEc2xrA2J1dHRvbg--;_ylc=X1MDOTYwNjI4NTcEX3IDMgRhY3RuA2NsawRiY2sDYmNxZHFiNWNyZ2dsdCUyNmIlM0QzJTI2cyUzRDUyBGNzcmNwdmlkAzUxUmw0ekl3Tmk2MmFicFpXYmhDdlFmME1UZ3pMZ0FBQUFEWldsaTUEZnIDeWZwLXQEZnIyA3NhLWdwBGdwcmlkAzhZX3NZNmcuUzJDRzNCTklsWlZFdUEEbXRlc3RpZANudWxsBG5fc3VnZwM0BG9yaWdpbgNpbWFnZXMuc2VhcmNoLnlhaG9vLmNvbQRwb3MDMARwcXN0cgMEcHFzdHJsAwRxc3RybAM4BHF1ZXJ5AKKKBHRfc3RtcAMxNTA1MjQ3OTkxBHZ0ZXN0aWQDbnVsbA--?gprid=8Y_sY6g.S2CG3BNIlZVEuA&pvid=51Rl4zIwNi62abpZWbhCvQf0MTgzLgAAAADZWli5&fr=yfp-t&fr2=sb-top-images.search.yahoo.com&ei=UTF-8&n=60&x=wrt&y=Search&p="
    private  final String ECODING = "UTF-8";
    private  final String IMGURL_REG = "<img.*src=(.*?)[^>]*?>";
    private  final String IMGSRC_REG = "http:\"?(.*?)(\"|>|\\s+)";
    private  String arm="";
    ArrayList<String> result = new ArrayList<String>();
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

    public ArrayList<String> getResult() {
        return result;
    }

    public String getHTML(String str) throws Exception { //这里为了方便处理
        URL uri = new URL(url + str);
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
//        System.out.println(sb.toString());
        ArrayList<String > arrayList = getImageUrl(sb.toString());
        result = arrayList;

        return sb.toString();
    }

    public ArrayList<String> getImageUrl(String HTML) {

        ArrayList<String> arrayList = new  ArrayList<String>();
        Pattern p = Pattern.compile("http://([^;\\s\"\'])*?\\.(jpg|png|jpeg|gif)");
        Matcher m = p.matcher(HTML);
//        s=m.replaceAll("</p><p>");

        while (m.find())
        {
            arrayList.add(m.group());
        }

        return  arrayList;
    }


    public ArrayList<String> getImageSrc(ArrayList<String> listImageUrl) {
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
