package com.tuotiansudai.exception;

import org.springframework.security.core.AuthenticationException;

public class ImageCaptchaException extends AuthenticationException {
    // ~ Constructors
    // ===================================================================================================

    /**
     * Constructs a <code>CaptchaNotMatchException</code> with the specified message.
     *
     * @param msg the detail message
     */
    public ImageCaptchaException(String msg) {
        super(msg);
    }

    /**
     * Constructs a <code>CaptchaNotMatchException</code> with the specified message and
     * root cause.
     *
     * @param msg the detail message
     * @param t root cause
     */
    public ImageCaptchaException(String msg, Throwable t) {
        super(msg, t);
    }
}