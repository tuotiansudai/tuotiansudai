package com.tuotiansudai.api.dto.v1_0;


import java.util.List;

public class MembershipResponseDataDto extends BaseResponseDataDto {
    private int index;
    private int pageSize;
    private long totalCount;
    private List<MembershipExperienceBillDataDto> membershipExperienceBill;

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

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public List<MembershipExperienceBillDataDto> getMembershipExperienceBill() {
        return membershipExperienceBill;
    }

    public void setMembershipExperienceBill(List<MembershipExperienceBillDataDto> membershipExperienceBill) {
        this.membershipExperienceBill = membershipExperienceBill;
    }
}
