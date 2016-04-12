package com.ttsd.api.dto;

import java.util.List;

public class ReferrerInvestListResponseDataDto extends BaseResponseDataDto{
    private Integer index;
    private Integer pageSize;
    private Integer totalCount;
    private String rewardTotalMoney;

    public String getRewardTotalMoney() {
        return rewardTotalMoney;
    }

    public void setRewardTotalMoney(String rewardTotalMoney) {
        this.rewardTotalMoney = rewardTotalMoney;
    }

    private List<ReferrerInvestResponseDataDto> referrerInvestList;

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
