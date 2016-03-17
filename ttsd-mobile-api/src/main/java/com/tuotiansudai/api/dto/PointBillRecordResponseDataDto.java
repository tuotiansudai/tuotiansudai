package com.tuotiansudai.api.dto;


public class PointBillRecordResponseDataDto extends BaseResponseDataDto {

    private String pointBillId;

    private String businessTypeName;

    private String businessType;

    private String point;

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
