package com.tuotiansudai.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class SmsUserReceiveMembershipDto implements Serializable {
    @NotEmpty
    @Pattern(regexp = "^\\d{11}$")
    private String mobile;

    private int level;

    public SmsUserReceiveMembershipDto() {
    }

    public SmsUserReceiveMembershipDto(String mobile, int level) {
        this.mobile = mobile;
        this.level = level;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
