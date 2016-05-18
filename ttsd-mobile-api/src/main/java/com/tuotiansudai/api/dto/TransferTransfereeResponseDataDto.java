package com.tuotiansudai.api.dto;


import java.util.List;

public class TransferTransfereeResponseDataDto extends BaseResponseDataDto {

    private Integer index;

    private Integer pageSize;

    private Integer totalCount;

    private List<TransferTransfereeRecordResponseDataDto> transferee;

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

    public List<TransferTransfereeRecordResponseDataDto> getTransferee() {
        return transferee;
    }

    public void setTransferee(List<TransferTransfereeRecordResponseDataDto> transferee) {
        this.transferee = transferee;
    }

    public TransferTransfereeResponseDataDto() {

    }

    public TransferTransfereeResponseDataDto(Integer index, Integer pageSize, Integer totalCount, List<TransferTransfereeRecordResponseDataDto> transferee) {
        this.index = index;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.transferee = transferee;
    }
}
