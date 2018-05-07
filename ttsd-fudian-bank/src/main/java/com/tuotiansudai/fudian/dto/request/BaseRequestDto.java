package com.tuotiansudai.fudian.dto.request;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.ExtMarkDto;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseRequestDto {

    private final static SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

    private transient long id;

    private String requestData;

    private String merchantNo; //商户号

    private String orderNo; //订单流水号

    private String orderDate = format.format(new Date()); //订单日期

    private String extMark; //参数扩展域

    public BaseRequestDto(ApiType apiType, String loginName, String mobile) {
        this.extMark = new Gson().toJson(new ExtMarkDto(loginName, mobile, apiType));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRequestData() {
        return requestData;
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getExtMark() {
        return extMark;
    }

    public void setExtMark(String extMark) {
        this.extMark = extMark;
    }
}
