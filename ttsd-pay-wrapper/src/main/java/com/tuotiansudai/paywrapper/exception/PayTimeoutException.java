package com.tuotiansudai.paywrapper.exception;

import com.tuotiansudai.constants.PayReturnCode;

public class PayTimeoutException extends PayException {
    public PayTimeoutException(Throwable cause) {
        super(PayReturnCode.ERROR_TIMEOUT, cause);
    }
}
