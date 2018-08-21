package com.tuotiansudai.fudian.umpdto;


import com.google.common.base.Strings;
import com.tuotiansudai.fudian.dto.BankDtoValidator;

import java.io.Serializable;

public class UmpUpdateMobileDto implements Serializable, BankDtoValidator {

    private long accountId;

    private String loginName;

    private String mobile;

    private String userName;

    private String identityNumber;

    public UmpUpdateMobileDto() {
    }

    public UmpUpdateMobileDto(long accountId, String loginName, String mobile, String userName, String identityNumber) {
        this.accountId = accountId;
        this.loginName = loginName;
        this.mobile = mobile;
        this.userName = userName;
        this.identityNumber = identityNumber;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    @Override
    public boolean isValid() {
        return accountId > 0
                && !Strings.isNullOrEmpty(mobile)
                && !Strings.isNullOrEmpty(loginName)
                && !Strings.isNullOrEmpty(identityNumber)
                && !Strings.isNullOrEmpty(userName);
    }
}
