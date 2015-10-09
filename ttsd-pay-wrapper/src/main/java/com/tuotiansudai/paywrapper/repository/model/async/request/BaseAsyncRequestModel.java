package com.tuotiansudai.paywrapper.repository.model.async.request;

import com.tuotiansudai.paywrapper.repository.model.sync.request.BaseSyncRequestModel;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

public abstract class BaseAsyncRequestModel extends BaseSyncRequestModel{

    static Logger logger = Logger.getLogger(BaseAsyncRequestModel.class);

    protected static Properties CALLBACK_HOST_PROPS = new Properties();

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
        if (CALLBACK_HOST_PROPS.isEmpty()) {
            try {
                Resource resourceService = new ClassPathResource("/ttsd-service.properties");
                BaseAsyncRequestModel.CALLBACK_HOST_PROPS = PropertiesLoaderUtils.loadProperties(resourceService);
            } catch (IOException e) {
                logger.error("ttsd-service.properties 不存在!");
                logger.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
