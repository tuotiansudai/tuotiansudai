package com.tuotiansudai.paywrapper.validation;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

public class ValidationReport {

    private String mustacheContext = "";

    private String title = "";

    private long count;

    private List<Map<String, Object>> summary = Lists.newArrayList();

    private List<Map<String, Object>> issueOrders = Lists.newArrayList();

    public String getMustacheContext() {
        return mustacheContext;
    }

    public void setMustacheContext(String mustacheContext) {
        this.mustacheContext = mustacheContext;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

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
