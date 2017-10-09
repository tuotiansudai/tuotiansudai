package com.tuotiansudai.paywrapper.repository.model.async.request;

import com.tuotiansudai.enums.AsyncUmPayService;
import com.tuotiansudai.paywrapper.repository.model.sync.request.BaseSyncRequestModel;
import com.tuotiansudai.repository.model.Source;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;

public abstract class BaseAsyncRequestModel extends BaseSyncRequestModel {

    private final static Logger logger = Logger.getLogger(BaseAsyncRequestModel.class);

    protected static Properties ENV_PROPS = new Properties();

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

    static {
        if (ENV_PROPS.isEmpty()) {
            try {
                Resource resourceService = new ClassPathResource("/ttsd-env.properties");
                BaseAsyncRequestModel.ENV_PROPS = PropertiesLoaderUtils.loadProperties(resourceService);
            } catch (IOException e) {
                logger.error("ttsd-env.properties 不存在!");
                logger.error(e.getLocalizedMessage(), e);
            }
        }
    }

    public BaseAsyncRequestModel() {
    }

    public BaseAsyncRequestModel(Source source, AsyncUmPayService service) {
        this.retUrl = Source.WEB == source ?
                MessageFormat.format("{0}/{1}", ENV_PROPS.get("pay.callback.web.host"), service.getWebRetCallbackPath())
                : MessageFormat.format("{0}/{1}", ENV_PROPS.get("pay.callback.app.web.host"), service.getMobileRetCallbackPath());
        this.notifyUrl = MessageFormat.format("{0}/{1}", ENV_PROPS.get("pay.callback.back.host"), service.getNotifyCallbackPath());
        this.setSourceV(source == Source.WEB ? null : "HTML5");
    }
}
