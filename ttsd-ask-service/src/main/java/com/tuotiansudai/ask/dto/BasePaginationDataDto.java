package com.tuotiansudai.ask.dto;

import com.tuotiansudai.ask.utils.PaginationUtil;

import java.util.List;

public class BasePaginationDataDto<T> extends BaseDataDto {

    private int index;

    private int pageSize;

    private long count;

    private int maxPage;

    private boolean hasPreviousPage;

    private boolean hasNextPage;

    protected List<T> records;

    public BasePaginationDataDto(int index, int pageSize, long count, List<T> records) {
        this.index = index;
        this.maxPage = PaginationUtil.calculateMaxPage(count, pageSize);
        this.pageSize = pageSize;
        this.count = count;
        this.hasPreviousPage = index > 1 && index <= maxPage;
        this.hasNextPage = index < maxPage;
        this.records = records;
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

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }
}
