package com.tuotiansudai.fudian.ump.sync.request;


import com.tuotiansudai.fudian.util.SyncUmPayService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class TranseqSearchRequestModel extends BaseSyncRequestModel {

    private String accountId;

    private String accountType; //01个人 02商户 03标的

    private String startDate; //YYYYMMDD

    private String endDate; //YYYYMMDD

    private String pageNum;

    public TranseqSearchRequestModel() {
    }

    public TranseqSearchRequestModel(String accountId, int pageNum, Date startDate, Date endDate) {
        super();
        this.service = SyncUmPayService.TRANSEQ_SEARCH.getServiceName();
        this.accountId = accountId;
        this.accountType = "01";
        this.startDate = new SimpleDateFormat("yyyyMMdd").format(startDate);
        this.endDate = new SimpleDateFormat("yyyyMMdd").format(endDate);
        this.pageNum = String.valueOf(pageNum);
    }

    @Override
    public Map<String, String> generatePayRequestData() {
        Map<String, String> payRequestData = super.generatePayRequestData();
        payRequestData.put("account_id", this.accountId);
        payRequestData.put("account_type", this.accountType);
        payRequestData.put("start_date", this.startDate);
        payRequestData.put("end_date", this.endDate);
        payRequestData.put("page_num", this.pageNum);
        return payRequestData;
    }
}
