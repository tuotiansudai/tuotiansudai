package com.tuotiansudai.exception;

/**
 * 存在等待状态的投资
 */
public class ExistWaitAffirmInvestsException extends TTSDException {

    public ExistWaitAffirmInvestsException() {
    }

    public ExistWaitAffirmInvestsException(String msg) {
        super(msg);
    }

    public ExistWaitAffirmInvestsException(String msg, Throwable e) {
        super(msg, e);
    }
}
