package com.tuotiansudai.dto;

import java.util.List;

/**
 * Created by Administrator on 2015/9/18.
 */
public class BasePaginationDataDto<T> extends BaseDataDto{

    private int index;
    private int pageSize;
    private long count;
    private boolean hasPreviousPage;
    private boolean hasNextPage;
    private List<T> records;

    public BasePaginationDataDto(int index, int pageSize, long count, List<T> records) {
        long totalPages = count / pageSize + (count % pageSize > 0 ? 1 : 0);
        this.index = index;
        this.pageSize = pageSize;
        this.count = count;
        this.hasPreviousPage = index > 1 && index <= totalPages;
        this.hasNextPage = index < totalPages;
        this.records = records;
    }

    public int getIndex() {
        return index;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getCount() {
        return count;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public List<T> getRecords() {
        return records;
    }
}
