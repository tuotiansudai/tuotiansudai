package com.tuotiansudai.fudian.dto;

import com.google.common.base.Strings;
import com.google.gson.GsonBuilder;

/**
 * Created by qduljs2011 on 2018/8/23.
 */
public class BankChangeMobileDto extends BankBaseDto {

    private String type;

    private String newPhone;

    public BankChangeMobileDto() {
    }

    public BankChangeMobileDto(String loginName, String mobile, String bankUserName, String bankAccountNo, String type, String newPhone) {
        super(loginName, mobile, bankUserName, bankAccountNo);
        this.type = type;
        this.newPhone = newPhone;
    }

    @Override
    public boolean isValid() {
        return !Strings.isNullOrEmpty(getLoginName())
                && !Strings.isNullOrEmpty(type)
                && !Strings.isNullOrEmpty(newPhone);
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }

    public String getType() {
        return type;
    }

    public String getNewPhone() {
        return newPhone;
    }
}
