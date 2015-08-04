package com.ttsd.api.dto;

import com.esoft.jdp2p.loan.LoanConstants;

public enum StandardStatus {
    RECHECK(LoanConstants.LoanStatus.RECHECK, "等待复核"),
    REPAYING(LoanConstants.LoanStatus.REPAYING, "回款中"),
    RAISING(LoanConstants.LoanStatus.RAISING, "抢投"),
    COMPLETE(LoanConstants.LoanStatus.COMPLETE,"已完成");
    String code;
    String message;

    StandardStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static String getMessageByCode(String code) {
        for (StandardStatus status : StandardStatus.values()) {
            if (status.code.equals(code)) {
                return status.message;
            }
        }
        return null;
    }
}
