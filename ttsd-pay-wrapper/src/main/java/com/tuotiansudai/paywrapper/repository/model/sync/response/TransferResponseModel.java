package com.tuotiansudai.paywrapper.repository.model.sync.response;

import java.util.Map;

public class TransferResponseModel extends BaseSyncResponseModel {

    private String orderId;

    private String merDate;

    private String tradeNo;

    private String merCheckDate;

    @Override
    public void initializeModel(Map<String, String> resData) {
        super.initializeModel(resData);
        this.orderId = resData.get("order_id");
        this.merDate = resData.get("mer_date");
        this.tradeNo = resData.get("trade_no");
        this.merCheckDate = resData.get("mer_check_date");
        
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

    public String getMerCheckDate() {
        return merCheckDate;
    }

    public void setMerCheckDate(String merCheckDate) {
        this.merCheckDate = merCheckDate;
    }
}
