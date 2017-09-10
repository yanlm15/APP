package com.bignewsmaker.makebignews;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Dell on 2017/9/10.
 * 可以用来测试
 */

public class Main {
    public static void main(String[] args) {
        System.out.println("233");
        String s = "http://himg2.huanqiu.com/attachment2010/2016/0912/20160912085920600.jpg";
        Pattern p = Pattern.compile("http://(\\w+|\\/|\\.|-)*.(jpg|png|jpeg|gif)");
        Matcher m = p.matcher(s);

        while( m.find())
            System.out.println(m.group());

    }
}
