package com.tuotiansudai.exception;

import java.text.DecimalFormat;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(){
        super();
    }

    public InsufficientBalanceException(String message){
        super(message);
    }

    public InsufficientBalanceException(String message, Throwable cause){
        super(message, cause);
    }
}
