package com.bignewsmaker.makebignews.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bignewsmaker.makebignews.basic_class.ConstData;

import java.util.List;

public class FragmentNewsAdapter extends FragmentPagerAdapter {
    private ConstData const_data = ConstData.getInstance();// 设置访问全局变量接口
    private String[] titles = new String[]{"全部", "推荐","科技", "教育", "军事", "国内", "社会", "文化", "汽车", "国际", "体育", "财经", "健康", "娱乐"};
    private List<Fragment> list;


    public FragmentNewsAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {

        return list.get(position);

    }

    @Override
    public int getCount() {
        return const_data.getTagSize();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        int i,temp=0;
        for (i = 0; i < 14; i++){
            if (const_data.getIstagSelected(i))
                temp++;
            if(temp==position+1)
                break;
        }
        return titles[i];
    }
}