package com.tuotiansudai.paywrapper.exception;

import com.tuotiansudai.constants.PayReturnCode;
import com.tuotiansudai.exception.BaseException;

public class PayException extends BaseException {

    private final PayReturnCode code;

    public PayException(String message) {
        super(message);
        this.code = PayReturnCode.ERROR;
    }

    public PayException(Throwable cause) {
        super(cause);
        this.code = PayReturnCode.ERROR;
    }

    public PayException(PayReturnCode code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public PayReturnCode getCode() {
        return code;
    }
}
