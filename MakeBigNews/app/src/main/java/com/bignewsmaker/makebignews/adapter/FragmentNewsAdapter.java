package com.bignewsmaker.makebignews.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class FragmentNewsAdapter extends FragmentPagerAdapter{
    private String[] titles=new String[]{"全部","科技","教育","军事","国内","社会","文化","汽车","国际","体育","财经","健康","娱乐"};
    private List<Fragment> list;
    private  int count = titles.length;






    public FragmentNewsAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list=list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);

    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}