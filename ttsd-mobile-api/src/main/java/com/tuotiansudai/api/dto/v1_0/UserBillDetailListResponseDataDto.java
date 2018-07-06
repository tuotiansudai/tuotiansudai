package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.repository.model.UserBillModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class UserBillDetailListResponseDataDto extends BaseResponseDataDto {
    private Integer index;
    private Integer pageSize;
    private Long totalCount;

    @ApiModelProperty(value = "资金明细记录", example = "list")
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

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
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
