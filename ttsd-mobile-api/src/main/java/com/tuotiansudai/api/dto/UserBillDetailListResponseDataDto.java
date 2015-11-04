package com.tuotiansudai.api.dto;

import com.tuotiansudai.repository.model.UserBillModel;

import java.util.List;

public class UserBillDetailListResponseDataDto extends BaseResponseDataDto {
    private Integer index;
    private Integer pageSize;
    private Integer totalCount;
    private List<UserBillRecordResponseDataDto> userBillList;

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

    public List<UserBillRecordResponseDataDto> getUserBillList() {
        return userBillList;
    }

    public void setUserBillList(List<UserBillRecordResponseDataDto> userBillList) {
        this.userBillList = userBillList;
    }

    public UserBillDetailListResponseDataDto(){

    }
    public UserBillDetailListResponseDataDto(UserBillModel userBillModel){

    }

}
