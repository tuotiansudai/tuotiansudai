package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class ReferrerInvestListResponseDataDto extends BaseResponseDataDto{
    private Integer index;
    private Integer pageSize;
    private Integer totalCount;

    @ApiModelProperty(value = "推荐人奖励", example = "100")
    private String rewardTotalMoney;

    @ApiModelProperty(value = "推荐人出借记录", example = "list")
    private List<ReferrerInvestResponseDataDto> referrerInvestList;

    public String getRewardTotalMoney() {
        return rewardTotalMoney;
    }

    public void setRewardTotalMoney(String rewardTotalMoney) {
        this.rewardTotalMoney = rewardTotalMoney;
    }

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

    public List<ReferrerInvestResponseDataDto> getReferrerInvestList() {
        return referrerInvestList;
    }

    public void setReferrerInvestList(List<ReferrerInvestResponseDataDto> referrerInvestList) {
        this.referrerInvestList = referrerInvestList;
    }
}
