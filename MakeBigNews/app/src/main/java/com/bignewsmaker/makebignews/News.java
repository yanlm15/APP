package com.bignewsmaker.makebignews;

import java.util.ArrayList;
import java.util.List;
/*
*
*
*/
public class News {
    private List<LIST> list = new ArrayList<LIST>();
    private int pageNo = 0;
    private int pageSize = 0;
    private int totalPages = 0;
    private int totalRecords = 0;

    public List<LIST> getList() {
        return list;
    }

    public void setList(List<LIST> list) {
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

}
