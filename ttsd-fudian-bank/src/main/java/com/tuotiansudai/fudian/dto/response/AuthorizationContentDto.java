package com.tuotiansudai.fudian.dto.response;

public class AuthorizationContentDto extends PayBaseContentDto {

    private String businessType;

    private String status; //1：成功授权；2：取消授权

    private String amount;

    private String endTime;

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isOpen(){
        return "1".equals(status);
    }

    public String getAmount() {
        return amount;
    }

    public String getEndTime() {
        return endTime;
    }
}
