package com.tuotiansudai.console.dto;

import com.tuotiansudai.repository.model.PayrollDetailModel;
import java.io.Serializable;
import java.util.List;

public class PayrollDataDto implements Serializable {
    private long id;
    private String title;
    private long totalAmount;
    private long headCount;
    private List<PayrollDetailModel> payrollDetailModelList;
    private boolean status;
    private String message;
    private String uuid;

    public PayrollDataDto() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PayrollDataDto(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public long getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public long getHeadCount() {
        return this.headCount;
    }

    public void setHeadCount(long headCount) {
        this.headCount = headCount;
    }

    public List<PayrollDetailModel> getPayrollDetailModelList() {
        return this.payrollDetailModelList;
    }

    public void setPayrollDetailModelList(List<PayrollDetailModel> payrollDetailModelList) {
        this.payrollDetailModelList = payrollDetailModelList;
    }

    public boolean isStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
