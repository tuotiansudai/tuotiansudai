package com.tuotiansudai.fudian.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "bank")
public class BankConfig {

    private String merchant;

    private String merchantUserName;

    private String merchantAccountNo;

    private String pfxPassword;

    private String pfxPath;

    private String cerPath;

    private String crlPath;

    private String bankUrl;

    private String callbackReturnUrl;

    private String callbackNotifyUrl;

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getMerchantUserName() {
        return merchantUserName;
    }

    public void setMerchantUserName(String merchantUserName) {
        this.merchantUserName = merchantUserName;
    }

    public String getMerchantAccountNo() {
        return merchantAccountNo;
    }

    public void setMerchantAccountNo(String merchantAccountNo) {
        this.merchantAccountNo = merchantAccountNo;
    }

    public String getPfxPassword() {
        return pfxPassword;
    }

    public void setPfxPassword(String pfxPassword) {
        this.pfxPassword = pfxPassword;
    }

    public String getPfxPath() {
        return pfxPath;
    }

    public void setPfxPath(String pfxPath) {
        this.pfxPath = pfxPath;
    }

    public String getCerPath() {
        return cerPath;
    }

    public void setCerPath(String cerPath) {
        this.cerPath = cerPath;
    }

    public String getCrlPath() {
        return crlPath;
    }

    public void setCrlPath(String crlPath) {
        this.crlPath = crlPath;
    }

    public String getBankUrl() {
        return bankUrl;
    }

    public void setBankUrl(String bankUrl) {
        this.bankUrl = bankUrl;
    }

    public String getCallbackReturnUrl() {
        return callbackReturnUrl;
    }

    public void setCallbackReturnUrl(String callbackReturnUrl) {
        this.callbackReturnUrl = callbackReturnUrl;
    }

    public String getCallbackNotifyUrl() {
        return callbackNotifyUrl;
    }

    public void setCallbackNotifyUrl(String callbackNotifyUrl) {
        this.callbackNotifyUrl = callbackNotifyUrl;
    }
}

