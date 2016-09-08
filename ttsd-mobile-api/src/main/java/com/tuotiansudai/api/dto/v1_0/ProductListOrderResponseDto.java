package com.tuotiansudai.api.dto.v1_0;


import java.util.List;

public class ProductListOrderResponseDto extends BaseResponseDataDto{
    private Integer index;
    private Integer pageSize;
    private Integer totalCount;
    private List<ProductOrderResponseDto> orders;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<ProductOrderResponseDto> getOrders() {
        return orders;
    }

    public void setOrders(List<ProductOrderResponseDto> orders) {
        this.orders = orders;
    }
}
