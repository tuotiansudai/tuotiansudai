package com.tuotiansudai.paywrapper.validation;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

public class ValidationReport {

    private List<Map<String, Object>> summary = Lists.newArrayList();

    private List<Map<String, Object>> issueOrders = Lists.newArrayList();

    public List<Map<String, Object>> getSummary() {
        return summary;
    }

    public void setSummary(List<Map<String, Object>> summary) {
        this.summary = summary;
    }

    public List<Map<String, Object>> getIssueOrders() {
        return issueOrders;
    }

    public void setIssueOrders(List<Map<String, Object>> issueOrders) {
        this.issueOrders = issueOrders;
    }
}
