package com.tuotiansudai.dto.sms;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class SmsUserReceiveMembershipDto implements Serializable {
    @NotEmpty
    @Pattern(regexp = "^\\d{11}$")
    private String mobile;

    private String level;

    public SmsUserReceiveMembershipDto() {
    }

    public SmsUserReceiveMembershipDto(String mobile, String level) {
        this.mobile = mobile;
        this.level = level;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
