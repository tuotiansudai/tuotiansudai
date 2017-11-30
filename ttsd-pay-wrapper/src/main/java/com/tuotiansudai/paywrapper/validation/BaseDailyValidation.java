package com.tuotiansudai.paywrapper.validation;

import com.google.common.collect.Maps;
import com.tuotiansudai.paywrapper.repository.mapper.DailyValidationMapper;
import com.tuotiansudai.paywrapper.repository.model.sync.response.TransferSearchResponseModel;
import com.tuotiansudai.paywrapper.service.UMPayRealTimeStatusService;
import com.tuotiansudai.repository.mapper.UserBillMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public abstract class BaseDailyValidation {

    protected DailyValidationMapper dailyValidationMapper;

    protected UMPayRealTimeStatusService umPayRealTimeStatusService;

    protected UserBillMapper userBillMapper;

    protected ValidationReport generateReport(String businessType, List<Map<String, String>> transactions) {
        ValidationReport report = new ValidationReport();

        for (Map<String, String> transaction : transactions) {
            String orderId = transaction.get("order_id");
            String amount = transaction.get("amount");
            String merDate = transaction.get("mer_date");
            try {
                TransferSearchResponseModel transferStatus = umPayRealTimeStatusService.getTransferStatus(orderId, merDate, businessType);
                this.addSummary(report, transferStatus.getRetCode());

                if (transferStatus.isSuccess()) {
                    if (!checkUserBill(orderId, amount)) {
                        this.addIssue(report, orderId, "用户交易记录异常");
                    }
                } else {
                    this.addIssue(report, orderId, transferStatus.getRetMsg());
                }
            } catch (Exception e) {
                this.addIssue(report, orderId, "查询失败");
                this.addSummary(report, "exception");
            }
        }
        return report;
    }

    abstract protected boolean checkUserBill(String orderId, String amount);

    protected void addIssue(ValidationReport report, String orderId, String issue) {
        HashMap<String, Object> newIssues = Maps.newHashMap();
        newIssues.put("orderId", orderId);
        newIssues.put("issue", issue);
        report.getIssueOrders().add(newIssues);
    }

    protected void addSummary(ValidationReport report, String code) {
        Optional<Map<String, Object>> optional = report.getSummary().stream().filter(summary -> summary.containsValue(code)).findFirst();
        if (optional.isPresent()) {
            Map<String, Object> objectMap = optional.get();
            objectMap.put("count", (Integer) objectMap.get("count") + 1);
        } else {
            HashMap<String, Object> newCode = Maps.newHashMap();
            newCode.put("code", code);
            newCode.put("count", 1);
            report.getSummary().add(newCode);
        }
    }
}
