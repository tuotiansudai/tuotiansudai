package com.tuotiansudai.paywrapper.security;

public class GHBSecurityException extends RuntimeException{
    public GHBSecurityException(String message) {
        super(message);
    }

    public GHBSecurityException(String message, Throwable cause) {
        super(message, cause);
    }
}
