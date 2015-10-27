package com.tuotiansudai.paywrapper.repository.model;

/**
 * 投资回调处理状态
 */
public enum InvestNotifyProcessStatus {
    /**
     * 未处理
     */
    NOT_DONE(0),
    /**
     * 已处理
     */
    DONE(1);

    private final int status;

    InvestNotifyProcessStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
