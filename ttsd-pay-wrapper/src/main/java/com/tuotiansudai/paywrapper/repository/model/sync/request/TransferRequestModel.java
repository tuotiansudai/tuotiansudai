package com.tuotiansudai.paywrapper.repository.model.sync.request;

import com.tuotiansudai.paywrapper.repository.model.UmPayParticAccType;
import com.tuotiansudai.paywrapper.repository.model.UmPayService;
import com.tuotiansudai.paywrapper.repository.model.UmPayTransAction;

import java.util.Map;

public class TransferRequestModel extends BaseSyncRequestModel {

    private String orderId;

    private String merDate;

    private String merAccountId;

    private String particAccType = UmPayParticAccType.PERSON.getCode();

    private String transAction = UmPayTransAction.OUT.getCode();

    private String particUserId;

    private String particAccountId;

    private String amount;

    public TransferRequestModel() {
    }

    public TransferRequestModel(String payUserId,String amount,String orderId) {
        super();
        this.service = UmPayService.TRANSFER.getServiceName();
        this.particUserId = payUserId;
        this.amount = amount;
        this.orderId = orderId;
    }

    @Override
    public Map<String, String> generatePayRequestData() {
        Map<String, String> payRequestData = super.generatePayRequestData();
        payRequestData.put("partic_acc_type", this.particAccType);
        payRequestData.put("trans_action", this.transAction);
        payRequestData.put("order_id",this.orderId);
        payRequestData.put("partic_user_id",this.particUserId);
        payRequestData.put("amount",this.amount);
        payRequestData.put("mer_date",this.merDate);

        return payRequestData;
    }


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getParticAccType() {
        return particAccType;
    }

    public void setParticAccType(String particAccType) {
        this.particAccType = particAccType;
    }

    public String getTransAction() {
        return transAction;
    }

    public void setTransAction(String transAction) {
        this.transAction = transAction;
    }

    public String getMerDate() {
        return merDate;
    }

    public void setMerDate(String merDate) {
        this.merDate = merDate;
    }

    public String getMerAccountId() {
        return merAccountId;
    }

    public void setMerAccountId(String merAccountId) {
        this.merAccountId = merAccountId;
    }

    public String getParticUserId() {
        return particUserId;
    }

    public void setParticUserId(String particUserId) {
        this.particUserId = particUserId;
    }

    public String getParticAccountId() {
        return particAccountId;
    }

    public void setParticAccountId(String particAccountId) {
        this.particAccountId = particAccountId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
