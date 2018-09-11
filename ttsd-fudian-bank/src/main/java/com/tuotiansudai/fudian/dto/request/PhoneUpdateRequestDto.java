package com.tuotiansudai.fudian.dto.request;

import java.util.Map;

public class PhoneUpdateRequestDto extends NotifyRequestDto {

    private String newPhone;

    private String type = "1"; // 1原有手机号可用，进行自助修改, 2原有手机号不可用，进行人工修改

    public PhoneUpdateRequestDto(Source source, String loginName, String mobile, String userName, String accountNo, String newPhone, Map<String, String> extraValues,String type) {
        super(source, loginName, mobile, userName, accountNo);
        this.newPhone = newPhone;
        this.type=type;
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