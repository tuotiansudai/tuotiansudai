package com.tuotiansudai.fudian.dto.response;

public class PhoneUpdateContentDto extends UserBaseContentDto {

    private String newPhone;

    private String type;

    private String status; //1代表更新手机号成功 2代表更新手机号失败

    public String getNewPhone() {
        return newPhone;
    }

    public void setNewPhone(String newPhone) {
        this.newPhone = newPhone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
