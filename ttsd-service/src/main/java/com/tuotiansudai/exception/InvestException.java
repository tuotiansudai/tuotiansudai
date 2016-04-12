package com.tuotiansudai.exception;

public class InvestException extends BaseException {
    private final InvestExceptionType type;

    public InvestException(InvestExceptionType exceptionType) {
        super(exceptionType.getDescription());
        this.type = exceptionType;
    }

    public InvestException(InvestExceptionType exceptionType, Throwable e) {
        super(exceptionType.getDescription());
        this.type = exceptionType;
    }

    public InvestExceptionType getType() {
        return type;
    }
}
