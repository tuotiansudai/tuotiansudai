package com.tuotiansudai.exception;

import org.springframework.security.authentication.BadCredentialsException;

/**
 * Created by Administrator on 2015/10/13.
 */
public class LoginFailedMaxTimesException extends BadCredentialsException {

    public LoginFailedMaxTimesException(String msg) {
        super(msg);
    }

    public LoginFailedMaxTimesException(String msg, Throwable t) {
        super(msg, t);
    }
}
