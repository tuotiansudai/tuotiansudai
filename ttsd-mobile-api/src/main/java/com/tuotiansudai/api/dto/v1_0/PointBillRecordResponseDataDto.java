package com.tuotiansudai.api.dto.v1_0;


import io.swagger.annotations.ApiModelProperty;

public class PointBillRecordResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "积分ID", example = "1")
    private String pointBillId;

    @ApiModelProperty(value = "积分类型名称", example = "签到奖励")
    private String businessTypeName;

    @ApiModelProperty(value = "积分类型", example = "SIGN_IN")
    private String businessType;

    @ApiModelProperty(value = "积分", example = "10")
    private String point;

    @ApiModelProperty(value = "创建时间", example = "2016-11-25 12:11:01")
    private String createdDate;

    public String getPointBillId() {
        return pointBillId;
    }

    public void setPointBillId(String pointBillId) {
        this.pointBillId = pointBillId;
    }

    public String getBusinessTypeName() {
        return businessTypeName;
    }

    public void setBusinessTypeName(String businessTypeName) {
        this.businessTypeName = businessTypeName;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
