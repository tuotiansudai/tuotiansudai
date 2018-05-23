package com.tuotiansudai.dto.sms;


import java.io.Serializable;
import java.util.List;

public class SmsPayrollNotifyDto implements Serializable {

    private List<String> mobiles;

    private String title;

    public SmsPayrollNotifyDto() {
    }

    public SmsPayrollNotifyDto(List<String> mobiles, String title) {
        this.mobiles = mobiles;
        this.title = title;
    }

    public List<String> getMobiles() {
        return mobiles;
    }

    public void setMobiles(List<String> mobiles) {
        this.mobiles = mobiles;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
