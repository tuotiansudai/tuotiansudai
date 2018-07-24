package com.tuotiansudai.fudian.ump.sync.request;

import com.tuotiansudai.fudian.util.SyncUmPayService;

import java.util.Map;

/**
 * Created by qduljs2011 on 2018/7/19.
 */
public class TransferSearchRequestModel extends BaseSyncRequestModel {
    private String orderId;

    private String merDate;

    private String busiType; //01充值 02提现 03标的转账 04转账

    public TransferSearchRequestModel() {
    }

    public TransferSearchRequestModel(String orderId, String merDate, String busiType) {
        this.service = SyncUmPayService.TRANSFER_SEARCH.getServiceName();
        this.orderId = orderId;
        this.merDate = merDate;
        this.busiType = busiType;
    }

    @Override
    public Map<String, String> generatePayRequestData() {
        Map<String, String> payRequestData = super.generatePayRequestData();
        payRequestData.put("order_id", this.orderId);
        payRequestData.put("mer_date", this.merDate);
        payRequestData.put("busi_type", this.busiType);
        return payRequestData;
    }
}
