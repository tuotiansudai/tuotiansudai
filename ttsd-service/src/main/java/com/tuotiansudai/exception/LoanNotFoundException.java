package com.tuotiansudai.exception;

public class LoanNotFoundException extends RuntimeException {
    public LoanNotFoundException(){
        super();
    }

    public LoanNotFoundException(String message){
        super(message);
    }

    public LoanNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
}
