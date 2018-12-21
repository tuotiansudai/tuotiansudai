package com.tuotiansudai.dto;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LoanRiskManagementDetailDto implements Serializable{

    private long id;
    private String title;
    private boolean checked;
    private List<String> details;

    public LoanRiskManagementDetailDto() {
    }

    public LoanRiskManagementDetailDto(long id, String title, boolean checked, String detail) {
        this.id = id;
        this.title = title;
        this.checked = checked;
        this.details = Strings.isNullOrEmpty(detail) ? new ArrayList<>() : Arrays.asList(detail .split(","));
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }
}
