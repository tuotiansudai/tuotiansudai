package com.tuotiansudai.api.dto.v1_0;


import io.swagger.annotations.ApiModelProperty;

public class ExperienceBillRecordResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "体验金ID", example = "1")
    private String experienceBillId;

    @ApiModelProperty(value = "操作类型", example = "IN")
    private String operationType;

    @ApiModelProperty(value = "体验金类型", example = "新用户注册")
    private String businessType;

    @ApiModelProperty(value = "金额", example = "10")
    private String amount;

    @ApiModelProperty(value = "创建时间", example = "2016-11-25 12:11:01")
    private String createdDate;

    public String getExperienceBillId() {
        return experienceBillId;
    }

    public void setExperienceBillId(String experienceBillId) {
        this.experienceBillId = experienceBillId;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
