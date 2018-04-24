package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

public class PhoneUpdateRequestDto extends UserBaseRequestDto {

    private String newPhone;

    private String type; // 1原有手机号可用，进行自助修改 2原有手机号不可用，进行人工修改

    public PhoneUpdateRequestDto(String userName, String accountNo, String newPhone, String type) {
        super(userName, accountNo, ApiType.PHONE_UPDATE.name());
        this.newPhone = newPhone;
        this.type = type;
    }

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
}