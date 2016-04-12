package com.tuotiansudai.paywrapper.exception;

import com.tuotiansudai.exception.BaseException;

public class PayException extends BaseException {

    public PayException(String message) {
        super(message);
    }

    public PayException(Throwable cause) {
        super(cause);
    }
}
