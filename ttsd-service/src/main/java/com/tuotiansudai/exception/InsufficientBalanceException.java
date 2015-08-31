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


    public static void main(String[] args) {
        DecimalFormat df = new DecimalFormat(".##");
        long amount = 23210330100L;
        String s = df.format(amount / 1000);
        System.out.println(s);
    }
}
