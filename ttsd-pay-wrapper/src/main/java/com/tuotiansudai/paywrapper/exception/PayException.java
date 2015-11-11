package com.tuotiansudai.paywrapper.exception;

public class PayException extends Exception {

    public PayException(String message) {
        super(message);
    }

    public PayException(Throwable cause) {
        super(cause);
    }
}
