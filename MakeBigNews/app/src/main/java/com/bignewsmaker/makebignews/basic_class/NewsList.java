package com.bignewsmaker.makebignews.basic_class;

import java.util.ArrayList;
import java.util.List;
/*
*
*
*/
public class NewsList {
    private List<News> list = new ArrayList<News>();
    private int pageNo = 0;
    private int pageSize = 0;
    private int totalPages = 0;
    private int totalRecords = 0;

    public List<News> getList() {
        return list;
    }

    public void setList(List<News> list) {
        this.list = list;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public void add_news(News cur)
    {
        list.add(cur);
    }

}
