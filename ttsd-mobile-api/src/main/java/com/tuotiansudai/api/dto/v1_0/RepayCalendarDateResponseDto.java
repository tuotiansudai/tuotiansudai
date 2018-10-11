package com.tuotiansudai.api.dto.v1_0;


import com.tuotiansudai.repository.model.RepayStatus;
import io.swagger.annotations.ApiModelProperty;

public class RepayCalendarDateResponseDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "标的名称", example = "100")
    private String loanName;

    @ApiModelProperty(value = "已收回款", example = "0")
    private String repayAmount;

    @ApiModelProperty(value = "待收回款", example = "100")
    private String expectedRepayAmount;

    @ApiModelProperty(value = "当前期数", example = "1")
    private String period;

    @ApiModelProperty(value = "总期数", example = "12")
    private String periods;

    @ApiModelProperty(value = "状态", example = "REPAYING")
    private String status;

    @ApiModelProperty(value = "状态描述", example = "已完成")
    private String statusDes;

    @ApiModelProperty(value = "投资ID", example = "1000")
    private String investId;

    @ApiModelProperty(value = "债权转让", example = "false")
    private boolean isTransferred;

    @ApiModelProperty(value = "债权转让ID", example = "1001")
    private String transferApplicationId;

    public RepayCalendarDateResponseDto() {
    }

    public RepayCalendarDateResponseDto(String loanName, String repayAmount, String expectedRepayAmount, String period, String periods, RepayStatus status, String investId, boolean isTransferred, String transferApplicationId) {
        this.loanName = loanName;
        this.repayAmount = repayAmount;
        this.expectedRepayAmount = expectedRepayAmount;
        this.period = period;
        this.periods = periods;
        this.status = status.name();
        this.statusDes = status == RepayStatus.REPAYING ? "待回款" : status == RepayStatus.COMPLETE ? "已回款" : status.getViewText();
        this.investId = investId;
        this.isTransferred = isTransferred;
        this.transferApplicationId = transferApplicationId;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public String getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(String repayAmount) {
        this.repayAmount = repayAmount;
    }

    public String getExpectedRepayAmount() {
        return expectedRepayAmount;
    }

    public void setExpectedRepayAmount(String expectedRepayAmount) {
        this.expectedRepayAmount = expectedRepayAmount;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getPeriods() {
        return periods;
    }

    public void setPeriods(String periods) {
        this.periods = periods;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInvestId() {
        return investId;
    }

    public void setInvestId(String investId) {
        this.investId = investId;
    }

    public boolean isTransferred() {
        return isTransferred;
    }

    public void setIsTransferred(boolean isTransferred) {
        this.isTransferred = isTransferred;
    }

    public String getTransferApplicationId() {
        return transferApplicationId;
    }

    public void setTransferApplicationId(String transferApplicationId) {
        this.transferApplicationId = transferApplicationId;
    }

    public String getStatusDes() {
        return statusDes;
    }

    public void setStatusDes(String statusDes) {
        this.statusDes = statusDes;
    }
}
