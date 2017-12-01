package com.tuotiansudai.paywrapper.validation;

import com.google.common.collect.Maps;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.paywrapper.repository.mapper.DailyValidationMapper;
import com.tuotiansudai.paywrapper.repository.model.sync.response.TransferSearchResponseModel;
import com.tuotiansudai.paywrapper.service.UMPayRealTimeStatusService;
import com.tuotiansudai.repository.mapper.UserBillMapper;
import com.tuotiansudai.repository.model.UserBillModel;
import com.tuotiansudai.repository.model.UserBillOperationType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public abstract class BaseDailyValidation {

    protected DailyValidationMapper dailyValidationMapper;

    protected UMPayRealTimeStatusService umPayRealTimeStatusService;

    protected UserBillMapper userBillMapper;


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
