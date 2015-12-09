package com.tuotiansudai.dto;


import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

public class CouponDto implements Serializable {
    @NotEmpty
    private String name;
    @NotEmpty
    @Pattern(regexp = "^\\d+\\.\\d{2}$")
    private String amount;
    @NotEmpty
    private Date startTime;
    @NotEmpty
    private Date endTime;
    @NotEmpty
    @Pattern(regexp = "^\\d+$")
    private long totalCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
}
