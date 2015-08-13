package com.tuotiansudai.paywrapper.repository.model.sync.request;

import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

public abstract class BaseSyncRequestModel {

    static Logger logger = Logger.getLogger(BaseSyncRequestModel.class);

    private static Properties props = new Properties();

    private Long id;

    protected String service;

    private String signType;

    private String charset;

    private String version;

    private String merchantId;

    private Date requestTime = new Date();

    private String requestData;

    private String requestUrl;

    private RequestStatus status = RequestStatus.READY;

    static {
        if (props.isEmpty()) {
            try {
                Resource resource = new ClassPathResource("/umpay.properties");
                BaseSyncRequestModel.props = PropertiesLoaderUtils.loadProperties(resource);
            } catch (IOException e) {
                logger.error("umpay.properties 不存在!");
                logger.error(e);
            }
        }
    }

    public BaseSyncRequestModel() {
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

    public Long getId() {
        return id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public String getRequestData() {
        return requestData;
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseSyncRequestModel that = (BaseSyncRequestModel) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
