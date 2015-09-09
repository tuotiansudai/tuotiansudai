package com.tuotiansudai.dto;

import java.util.List;

public class BasePaginationDto<T extends BasePaginationDataDto> extends BaseDataDto{
    private int index;
    private int pageSize;
    private int totalCount;
    private int totalPages;
    private boolean hasPreviousPage;
    private boolean hasNextPage;

    private List<T> recordDtoList;

    public BasePaginationDto(){

    }
    public BasePaginationDto(int index, int pageSize, int totalCount){
        this.index = index;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        if(totalCount % pageSize > 0){
            this.totalPages = totalCount/pageSize + 1;
        }else{
            this.totalPages = totalCount/pageSize;
        }
        this.isHasNextPage();
        this.isHasPreviousPage();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<T> getRecordDtoList() {
        return recordDtoList;
    }

    public void setRecordDtoList(List<T> recordDtoList) {
        this.recordDtoList = recordDtoList;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isHasPreviousPage() {

        return hasPreviousPage = this.index > 1;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean isHasNextPage() {

        return hasNextPage = this.index >= 1 && this.index < this.totalPages;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }


}
