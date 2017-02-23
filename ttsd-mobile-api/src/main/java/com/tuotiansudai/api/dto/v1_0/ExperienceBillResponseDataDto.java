package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class ExperienceBillResponseDataDto extends BaseResponseDataDto {

    private Integer index;
    private Integer pageSize;
    private Long totalCount;

    @ApiModelProperty(value = "体验金明细", example = "list")
    private List<ExperienceBillRecordResponseDataDto> experienceBills;

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

    public List<ExperienceBillRecordResponseDataDto> getExperienceBills() {
        return experienceBills;
    }

    public void setExperienceBills(List<ExperienceBillRecordResponseDataDto> experienceBills) {
        this.experienceBills = experienceBills;
    }
}
