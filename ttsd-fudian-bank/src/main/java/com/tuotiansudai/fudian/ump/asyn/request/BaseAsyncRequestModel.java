package com.tuotiansudai.fudian.ump.asyn.request;


import com.tuotiansudai.fudian.ump.AsyncUmPayService;
import com.tuotiansudai.fudian.ump.sync.request.BaseSyncRequestModel;

import java.text.MessageFormat;

public abstract class BaseAsyncRequestModel extends BaseSyncRequestModel {

    protected String retUrl;

    protected String notifyUrl;

    public String getRetUrl() {
        return retUrl;
    }

    public void setRetUrl(String retUrl) {
        this.retUrl = retUrl;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public BaseAsyncRequestModel() {
    }

    public BaseAsyncRequestModel(AsyncUmPayService service) {
        this.retUrl = MessageFormat.format("{0}/{1}", PAY_CALLBACK_WEB_HOST, service.getWebRetCallbackPath());
        this.notifyUrl = MessageFormat.format("{0}/{1}", PAY_CALLBACK_BACK_HOST, service.getNotifyCallbackPath());
    }
}
