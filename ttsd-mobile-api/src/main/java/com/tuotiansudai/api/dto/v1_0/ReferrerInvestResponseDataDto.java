package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.repository.model.ReferrerManageView;
import com.tuotiansudai.repository.model.ReferrerRewardStatus;
import com.tuotiansudai.util.AmountConverter;
import io.swagger.annotations.ApiModelProperty;

import java.text.SimpleDateFormat;

public class ReferrerInvestResponseDataDto {

    @ApiModelProperty(value = "被推荐人", example = "wangtuotian")
    private String userId;

    @ApiModelProperty(value = "被推荐人层级", example = "1")
    private String level;

    @ApiModelProperty(value = "标的id", example = "10010")
    private String loanId;

    @ApiModelProperty(value = "投标标的", example = "房屋抵押")
    private String loanName;

    @ApiModelProperty(value = "出借金额", example = "100")
    private String investMoney;

    @ApiModelProperty(value = "期数", example = "3")
    private String deadline;

    @ApiModelProperty(value = "出借时间", example = "2016-11-25 14:32:01")
    private String investTime;

    @ApiModelProperty(value = "奖励金额", example = "100")
    private String rewardMoney;

    @ApiModelProperty(value = "奖励时间", example = "2016-11-25 14:32:01")
    private String rewardTime;

    @ApiModelProperty(value = "标的类型", example = "_30")
    private String productNewType;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public String getInvestMoney() {
        return investMoney;
    }

    public void setInvestMoney(String investMoney) {
        this.investMoney = investMoney;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getInvestTime() {
        return investTime;
    }

    public void setInvestTime(String investTime) {
        this.investTime = investTime;
    }

    public String getRewardMoney() {
        return rewardMoney;
    }

    public void setRewardMoney(String rewardMoney) {
        this.rewardMoney = rewardMoney;
    }

    public String getRewardTime() {
        return rewardTime;
    }

    public void setRewardTime(String rewardTime) {
        this.rewardTime = rewardTime;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getProductNewType() {
        return productNewType;
    }

    public void setProductNewType(String productNewType) {
        this.productNewType = productNewType;
    }

    public ReferrerInvestResponseDataDto() {

    }

    public ReferrerInvestResponseDataDto(ReferrerManageView input) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.userId = input.getInvestName();
        this.level = "" + input.getLevel();
        this.loanName = input.getLoanName();
        this.investMoney = AmountConverter.convertCentToString(input.getInvestAmount());
        this.deadline = "" + input.getPeriods();
        this.investTime = simpleDateFormat.format(input.getInvestTime());
        this.rewardTime = simpleDateFormat.format(input.getRewardTime());
        if(ReferrerRewardStatus.FORBIDDEN == input.getStatus()){
            this.rewardMoney = "0.00";
        }else {
            this.rewardMoney = AmountConverter.convertCentToString(input.getRewardAmount());
        }
        this.loanId = input.getLoanId();
        this.productNewType = input.getProductType() != null ? input.getProductType().name() : "";
    }

}
