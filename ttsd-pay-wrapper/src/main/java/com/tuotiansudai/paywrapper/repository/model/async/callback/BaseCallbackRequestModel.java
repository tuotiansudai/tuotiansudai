package com.tuotiansudai.paywrapper.repository.model.async.callback;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

public class BaseCallbackRequestModel {

    static Logger logger = Logger.getLogger(BaseCallbackRequestModel.class);

    public static String SUCCESS_CODE = "0000";

    private static Properties props = new Properties();

    private long id;
    private String service;
    private String signType;
    private String sign;
    private String merId;
    private String version;
    private String orderId;
    private String merDate;
    private String tradeNo;
    private String retCode;
    private String retMsg;
    private String charset;

    private Date requestTime = new Date();
    private Date responseTime;
    private String requestData;
    private String responseData;

    public boolean isSuccess() {
        return SUCCESS_CODE.equals(retCode);
    }

    static {
        if (props.isEmpty()) {
            try {
                Resource resource = new ClassPathResource("SignVerProp.properties");
                BaseCallbackRequestModel.props = PropertiesLoaderUtils.loadProperties(resource);
            } catch (IOException e) {
                logger.error("SignVerProp.properties 不存在!");
                logger.error(e.getLocalizedMessage(), e);
            }
        }
    }

    public Map<String, String> generatePayResponseData() {
        Map<String, String> payRequestData = Maps.newHashMap();
        payRequestData.put("sign_type", props.getProperty("sign_type"));
        payRequestData.put("mer_id", props.getProperty("mer_id"));
        payRequestData.put("version", props.getProperty("version"));
        if(StringUtils.isNotEmpty(this.orderId)){
            payRequestData.put("order_id", this.orderId);
        }
        if(StringUtils.isNotEmpty(this.merDate)){
            payRequestData.put("mer_date", this.merDate);
        }
        payRequestData.put("ret_code", SUCCESS_CODE);
        return payRequestData;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
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

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public Date getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Date responseTime) {
        this.responseTime = responseTime;
    }

    public String getRequestData() {
        return requestData;
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }
    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

}
