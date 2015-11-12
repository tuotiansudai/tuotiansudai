package com.tuotiansudai.exception;

import org.springframework.security.core.AuthenticationException;

public class CaptchaNotMatchException extends AuthenticationException {
    // ~ Constructors
    // ===================================================================================================

    /**
     * Constructs a <code>CaptchaNotMatchException</code> with the specified message.
     *
     * @param msg the detail message
     */
    public CaptchaNotMatchException(String msg) {
        super(msg);
    }

    /**
     * Constructs a <code>CaptchaNotMatchException</code> with the specified message and
     * root cause.
     *
     * @param msg the detail message
     * @param t root cause
     */
    public CaptchaNotMatchException(String msg, Throwable t) {
        super(msg, t);
    }
}