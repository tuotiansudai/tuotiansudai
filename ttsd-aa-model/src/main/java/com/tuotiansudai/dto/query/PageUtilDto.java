package com.tuotiansudai.dto.query;

import java.io.Serializable;

/**
 * Created by qduljs2011 on 2018/7/6.
 */
public class PageUtilDto implements Serializable{
    //每页数据量
    private int pageSize=10;

    //实际总的数据量需要传进来
    private int realCount;

    //当前页码 默认为1
    private int pageNumber=1;

    //开始索引
    private int startRow;

    public int getPageSize() {
        if(pageSize == 0){
            return 10;
        }
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getRealCount() {
        return realCount;
    }

    public void setRealCount(int realCount) {
        this.realCount = realCount;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getStartRow() {
        return (getPageNumber()-1)*getPageSize();
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

   public int getPageCount(){
       return (getRealCount() % getPageSize() ==0 ? getRealCount() / getPageSize() : getRealCount() / getPageSize()+1);
   }

   public boolean hasNextPage(){
       return getPageNumber()<getPageCount();
   }

   public boolean hasPreviousPage(){
       return getPageNumber()>1;
   }


}
