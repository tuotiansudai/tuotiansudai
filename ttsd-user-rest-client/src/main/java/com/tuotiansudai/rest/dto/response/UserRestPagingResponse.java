package com.tuotiansudai.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UserRestPagingResponse<T> extends UserRestResponseBase {
    @JsonProperty("total_count")
    private int totalCount;
    private int page;
    @JsonProperty("page_size")
    private int pageSize;
    @JsonProperty("has_next")
    private boolean hasNext;
    @JsonProperty("has_prev")
    private boolean hasPrev;
    private List<T> items;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public boolean isHasPrev() {
        return hasPrev;
    }

    public void setHasPrev(boolean hasPrev) {
        this.hasPrev = hasPrev;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
