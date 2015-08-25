package com.tuotiansudai.paywrapper.exception;

public class AmountTransferException extends Exception {

    public AmountTransferException() {
        super();
    }

    public AmountTransferException(String message) {
        super(message);
    }

    public AmountTransferException(String message, Throwable cause) {
        super(message, cause);
    }

    public AmountTransferException(Throwable cause) {
        super(cause);
    }

    protected AmountTransferException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
