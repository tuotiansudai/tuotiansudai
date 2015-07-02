package com.tuotiansudai.paywrapper.repository.model;

import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

public class BaseRequestModel {

    static Logger logger = Logger.getLogger(BaseRequestModel.class);

    private static Properties props;

    protected String service;

    private String signType;

    private String sign;

    private String charset;

    private String version;

    private String merchantId;

    private Date requestTime = new Date();

    private String requestUrl;

    public BaseRequestModel() {
        if (props == null) {
            try {
                Resource resource = new ClassPathResource("/umpay.properties");
                BaseRequestModel.props = PropertiesLoaderUtils.loadProperties(resource);
            } catch (IOException e) {
                logger.error("umpay.properties 不存在!");
                logger.error(e);
            }
        }

        this.signType = props.getProperty("sign_type");
        this.charset = props.getProperty("charset");
        this.version = props.getProperty("version");
        this.merchantId = props.getProperty("mer_id");
    }

    public Map<String, String> generatePayRequestData() {
        Map<String, String> payRequestData = Maps.newHashMap();
        payRequestData.put("service", this.service);
        payRequestData.put("sign_type", this.signType);
        payRequestData.put("charset", this.charset);
        payRequestData.put("mer_id", this.merchantId);
        payRequestData.put("version", this.version);
        return payRequestData;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }
}
