package com.tuotiansudai.api.dto.v1_0;


import java.util.List;

public class MembershipResponseDataDto extends BaseResponseDataDto {
    private long index;
    private long pageSize;
    private long totalCount;
    private List<MembershipExperienceBillDataDto> membershipExperienceBill;

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
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
