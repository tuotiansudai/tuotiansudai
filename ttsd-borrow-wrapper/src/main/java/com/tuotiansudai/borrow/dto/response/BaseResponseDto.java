package com.tuotiansudai.borrow.dto.response;


import java.io.Serializable;

public class BaseResponseDto implements Serializable{

    private boolean status;

    private String message;

    public BaseResponseDto(){

    }

    public BaseResponseDto(boolean status){
        this.status = status;
    }

    public BaseResponseDto(String message){
        this.status = false;
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
