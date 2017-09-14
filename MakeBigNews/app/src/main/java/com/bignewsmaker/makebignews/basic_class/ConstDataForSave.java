package com.bignewsmaker.makebignews.basic_class;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Dell on 2017/9/13.
 */

public class ConstDataForSave extends DataSupport {

    //6
    private boolean show_picture = true; //图片显示标签
    private boolean isDay = true;
    private Set<String> filtered=new HashSet<>();
    private Set<String>  dislike = new HashSet<>();// 不喜欢的词条
    private List<Integer> istagselected = new ArrayList<>();
    private Set<String> haveRead=new HashSet<>();
    private Set<String> like=new HashSet<>();
    private String cur_pageSize =null;
    private Set<String> isDownload=new HashSet<>();


    public Set<String> getIsDownload() {
        return isDownload;
    }

    public void setIsDownload(Set<String> isDownload) {
        this.isDownload = isDownload;
    }

    public List<Integer> getIstagselected() {
        return istagselected;
    }

    public void setIstagselected(List<Integer> istagselected) {
        this.istagselected = istagselected;
    }

    public String getCur_pageSize() {
        return cur_pageSize;
    }

    public void setCur_pageSize(String cur_pageSize) {
        this.cur_pageSize = cur_pageSize;
    }



    public Set<String> getLike() {
        return like;
    }

    public void setLike(Set<String> like) {
        this.like = like;
    }

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

    public Set<String> getFiltered() {
        return filtered;
    }

    public void setFiltered(Set<String> filtered) {
        this.filtered = filtered;
    }

    public Set<String>  getDislike() {
        return dislike;
    }

    public void setDislike(Set<String>  dislike) {
        this.dislike = dislike;
    }

    public Set<String> getHaveRead() {
        return haveRead;
    }

    public void setHaveRead(Set<String> haveRead) {
        this.haveRead = haveRead;
    }

}
