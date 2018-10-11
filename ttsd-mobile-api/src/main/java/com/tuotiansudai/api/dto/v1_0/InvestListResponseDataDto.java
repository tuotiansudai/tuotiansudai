package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class InvestListResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "起始", example = "1")
    private Integer index;

    @ApiModelProperty(value = "条数", example = "20")
    private Integer pageSize;

    @ApiModelProperty(value = "总计", example = "20")
    private Integer totalCount;

    @ApiModelProperty(value = "出借记录", example = "list")
    private List<InvestRecordResponseDataDto> investRecord;

    @ApiModelProperty(value = "标王信息", example = "list")
    private List<LoanAchievementsResponseDto> achievements;

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

    public List<InvestRecordResponseDataDto> getInvestRecord() {
        return investRecord;
    }

    public void setInvestRecord(List<InvestRecordResponseDataDto> investRecord) {
        this.investRecord = investRecord;
    }

    public List<LoanAchievementsResponseDto> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<LoanAchievementsResponseDto> achievements) {
        this.achievements = achievements;
    }
}
