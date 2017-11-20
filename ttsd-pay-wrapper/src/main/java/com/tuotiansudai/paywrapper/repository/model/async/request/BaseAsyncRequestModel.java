package com.tuotiansudai.paywrapper.repository.model.async.request;

import com.tuotiansudai.enums.AsyncUmPayService;
import com.tuotiansudai.paywrapper.repository.model.sync.request.BaseSyncRequestModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.util.ETCDConfigReader;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;

public abstract class BaseAsyncRequestModel extends BaseSyncRequestModel {

    private final static Logger logger = Logger.getLogger(BaseAsyncRequestModel.class);

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

    public BaseAsyncRequestModel(Source source, AsyncUmPayService service) {
        this.retUrl = Source.WEB == source ?
                MessageFormat.format("{0}/{1}", ETCDConfigReader.getValue("pay.callback.web.host"), service.getWebRetCallbackPath())
                : MessageFormat.format("{0}/{1}", ETCDConfigReader.getValue("pay.callback.app.web.host"), service.getMobileRetCallbackPath());
        this.notifyUrl = MessageFormat.format("{0}/{1}", ETCDConfigReader.getValue("pay.callback.back.host"), service.getNotifyCallbackPath());
        this.setSourceV(source == Source.WEB ? null : "HTML5");
    }
}
