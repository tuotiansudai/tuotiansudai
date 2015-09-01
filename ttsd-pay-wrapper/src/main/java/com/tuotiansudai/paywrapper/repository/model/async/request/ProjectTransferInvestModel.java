package com.tuotiansudai.paywrapper.repository.model.async.request;

import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.paywrapper.repository.model.UmPayParticAccType;
import com.tuotiansudai.paywrapper.repository.model.UmPayParticType;
import com.tuotiansudai.paywrapper.repository.model.UmPayServType;
import com.tuotiansudai.paywrapper.repository.model.UmPayTransAction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ProjectTransferInvestModel extends BaseAsyncModel {
    private String loanId;
    private String orderId;
    private String merDate;
    private String umPayUserId;
    private String amount;

    public ProjectTransferInvestModel() {

    }

    public ProjectTransferInvestModel(String loanId, String orderId, String umPayUserId, String amount) {
        super();
        this.service = "project_transfer_invest";
        this.retUrl = "http://localhost:8080";
        this.notifyUrl = "http://121.43.71.173:13002/trusteeship_return_s2s/invest_notify";
        this.orderId = orderId;//invest.getId()
        this.loanId = loanId;
        this.umPayUserId= umPayUserId;//getTrusteeshipAccount(invest.getUser().getId()).getId()
        this.amount = amount;//currentNumberFormat.format(invest.getInvestMoney() * 100)
        this.merDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    public Map<String, String> generatePayRequestData() {
        Map<String, String> payRequestData = super.generatePayRequestData();
        payRequestData.put("ret_url", "/");
        payRequestData.put("notify_url", notifyUrl);
        payRequestData.put("order_id", orderId);
        payRequestData.put("mer_date", merDate);
        payRequestData.put("project_id", loanId);
        payRequestData.put("serv_type", UmPayServType.TRANSFER_IN_INVEST.getCode());
        payRequestData.put("trans_action", UmPayTransAction.IN.getCode());
        payRequestData.put("partic_type", UmPayParticType.INVESTOR.getCode());
        payRequestData.put("partic_acc_type", UmPayParticAccType.PERSON.getCode());
        payRequestData.put("partic_user_id", umPayUserId);
        payRequestData.put("amount",amount);
        return payRequestData;
    }
}
