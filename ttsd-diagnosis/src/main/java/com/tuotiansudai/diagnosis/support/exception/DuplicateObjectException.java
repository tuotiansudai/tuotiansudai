package com.tuotiansudai.diagnosis.support.exception;

public class DuplicateObjectException extends RuntimeException {
    public DuplicateObjectException(String messageFormat, Object... args) {
        super(String.format(messageFormat, args));
    }
}
