package com.tuotiansudai.api.dto.v1_0;

import java.util.List;

public class ActivityCenterResponseDto extends BaseResponseDataDto {
    private Integer index;
    private Integer pageSize;
    private Integer totalCount;
    private List<ActivityCenterDataDto> activities;

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

    public List<ActivityCenterDataDto> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityCenterDataDto> activities) {
        this.activities = activities;
    }
}
