package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

import java.util.Map;

public class PayBaseRequestDto extends BaseRequestDto {

    private String userName; //用户名

    private String accountNo; //账户号

    private String returnUrl; //同步回调地址

    private String notifyUrl; //异步回调地址

    public PayBaseRequestDto() {
    }

    public PayBaseRequestDto(Source source, String loginName, String mobile, String userName, String accountNo, ApiType apiType, Map<String, String> extraValues) {
        super(source, loginName, mobile, apiType, extraValues);
        this.userName = userName;
        this.accountNo = accountNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
}
