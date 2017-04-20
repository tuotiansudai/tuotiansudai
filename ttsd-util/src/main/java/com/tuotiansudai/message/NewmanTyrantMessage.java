package com.tuotiansudai.message;


import java.io.Serializable;
import java.util.Date;

public class NewmanTyrantMessage implements Serializable {

    private Date currentDate;
    private String loginName;

    public NewmanTyrantMessage(Date currentDate,String loginName){
        this.currentDate = currentDate;
        this.loginName = loginName;
    }
    public NewmanTyrantMessage(){}

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
