package com.bignewsmaker.makebignews;

/**
 * Created by liminyan on 06/09/2017.
 * 这个类的作用是储存全局的设置变量
 * 我原来的意思是老张你把这个类写了就行，然后随便画画界面，然后能够交互就行
 * 其他的activity 可以访问这个类
 * 通过set 来设置
 * 通过get 来访问
 *
 */

public class ConstData {
    private double ligth_rate = 1; //屏幕亮度比例
    private boolean show_picture = true; //图片显示标签

    private static ConstData cur;

    public void init()
    {
        double ligth_rate = 1;
        boolean show_picture = true;
    }

    public void setLigth_rate(double ligth_rate) {
        this.ligth_rate = ligth_rate;
    }

    public void setShow_picture(boolean show_picture) {
        this.show_picture = show_picture;
    }

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
