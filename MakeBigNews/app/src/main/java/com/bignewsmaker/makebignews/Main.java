package com.bignewsmaker.makebignews;

/**
 * Created by Dell on 2017/9/10.
 * 可以用来测试
 */

public class Main {
    public static void main(String[] args) {
        System.out.println("233");
        String[] s="http://upload.qianlong.com/2016/0912/1473661206139.jpg http://upload.qianlong.com/2016/0912/1473665750616.jpg".split(";|\\s");
        for(String e:s)
            System.out.println(e);
    }
}
