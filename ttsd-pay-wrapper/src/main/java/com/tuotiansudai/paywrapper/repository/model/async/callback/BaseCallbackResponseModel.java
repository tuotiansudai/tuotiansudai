package com.tuotiansudai.paywrapper.repository.model.async.callback;

import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

public class BaseCallbackResponseModel {

    static Logger logger = Logger.getLogger(BaseCallbackResponseModel.class);

    private static Properties props = new Properties();

    private Long id;

    private Long requestId;

    private String signType;

    private String merId;

    private String version;

    private String orderId;

    private String merDate;

    private String retCode = "0000";

    private String responseData;

    private Date responseTime = new Date();

    static {
        if (props.isEmpty()) {
            try {
                Resource resource = new ClassPathResource("/umpay.properties");
                BaseCallbackResponseModel.props = PropertiesLoaderUtils.loadProperties(resource);
            } catch (IOException e) {
                logger.error("umpay.properties 不存在!");
                logger.error(e);
            }
        }
    }

    public BaseCallbackResponseModel(Long requestId, String orderId, String merDate) {
        this.signType = props.getProperty("sign_type");
        this.version = props.getProperty("version");
        this.merId = props.getProperty("mer_id");
        this.requestId = requestId;
        this.orderId = orderId;
        this.merDate = merDate;
    }

    public BaseCallbackResponseModel() {
    }

    public Map<String, String> generatePayResponseData() {
        Map<String, String> payRequestData = Maps.newHashMap();
        payRequestData.put("sign_type", this.signType);
        payRequestData.put("mer_id", this.merId);
        payRequestData.put("version", this.version);
        payRequestData.put("order_id", this.orderId);
        payRequestData.put("mer_date", this.merDate);
        payRequestData.put("ret_code", this.retCode);
        return payRequestData;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMerDate() {
        return merDate;
    }

    public void setMerDate(String merDate) {
        this.merDate = merDate;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    public Date getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Date responseTime) {
        this.responseTime = responseTime;
    }
}
