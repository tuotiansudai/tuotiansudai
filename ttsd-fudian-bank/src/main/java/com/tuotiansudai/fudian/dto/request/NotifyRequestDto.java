package com.tuotiansudai.fudian.dto.request;

public class NotifyRequestDto extends BaseRequestDto {

    private String returnUrl; //同步回调地址

    private String notifyUrl; //异步回调地址

    public NotifyRequestDto() {
    }

    public NotifyRequestDto(Source source, String loginName, String mobile, String userName, String accountNo) {
        super(source, loginName, mobile, userName, accountNo);
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
