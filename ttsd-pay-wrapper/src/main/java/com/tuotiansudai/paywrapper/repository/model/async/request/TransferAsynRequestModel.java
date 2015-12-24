package com.tuotiansudai.paywrapper.repository.model.async.request;

import com.tuotiansudai.paywrapper.repository.model.UmPayService;
import com.tuotiansudai.repository.model.Source;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class TransferAsynRequestModel extends BaseAsyncRequestModel {


    private String orderId;

    private String merAccountId;

    private String particUserId;

    private String particAccountId;

    private String particAccType = "01";

    private String merDate;

    private String amount;

    public TransferAsynRequestModel(){

    }
    public TransferAsynRequestModel(String orderId,String payUserId,String amount){
        this.service = "transfer_asyn";
        this.orderId = orderId;
        this.merDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        this.particUserId = payUserId;
        this.amount = amount;
        this.retUrl = MessageFormat.format("{0}/finance-manage/system-bill", CALLBACK_HOST_PROPS.get("pay.callback.console.host"));
        this.notifyUrl = MessageFormat.format("{0}/{1}", CALLBACK_HOST_PROPS.get("pay.callback.back.host"), "transfer_notify");
    }



    @Override
    public Map<String, String> generatePayRequestData() {
        Map<String, String> payRequestData = super.generatePayRequestData();
        payRequestData.put("ret_url", this.retUrl);
        payRequestData.put("notify_url", this.notifyUrl);
        payRequestData.put("order_id", this.orderId);
        payRequestData.put("mer_date", this.merDate);
        payRequestData.put("partic_user_id", this.particUserId);
        payRequestData.put("partic_acc_type", this.particAccType);
        payRequestData.put("amount", this.amount);
        return payRequestData;
    }


}
