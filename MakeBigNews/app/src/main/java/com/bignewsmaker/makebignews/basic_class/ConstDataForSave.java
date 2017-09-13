package com.bignewsmaker.makebignews.basic_class;

import org.litepal.crud.DataSupport;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by Dell on 2017/9/13.
 */

public class ConstDataForSave extends DataSupport {

    //6
    private boolean show_picture = true; //图片显示标签
    private boolean isDay = true;
    private HashSet<String> filtered=new HashSet<>();
    private TreeMap<String,Integer> dislike = new TreeMap<String,Integer>();// 不喜欢的词条
    private boolean[] istagSelected={true,true,true,true,true,true,true,true,true,true,true,true,true,true};
    private Set<String> haveRead=new HashSet<>();


    public boolean isShow_picture() {
        return show_picture;
    }

    public void setShow_picture(boolean show_picture) {
        this.show_picture = show_picture;
    }

    public boolean isDay() {
        return isDay;
    }

    public void setDay(boolean day) {
        isDay = day;
    }

    public HashSet<String> getFiltered() {
        return filtered;
    }

    public void setFiltered(HashSet<String> filtered) {
        this.filtered = filtered;
    }

    public TreeMap<String, Integer> getDislike() {
        return dislike;
    }

    public void setDislike(TreeMap<String, Integer> dislike) {
        this.dislike = dislike;
    }

    public boolean getIstagSelected(int index) {
        return istagSelected[index];
    }

    public void setIstagSelected(int index, boolean isSelected) {
        istagSelected[index] = isSelected;
    }

    public Set<String> getHaveRead() {
        return haveRead;
    }

    public void setHaveRead(Set<String> haveRead) {
        this.haveRead = haveRead;
    }

}
