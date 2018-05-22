package com.tuotiansudai.dto.sms;


import java.io.Serializable;
import java.util.List;

public class SmsPayrollNotifyDto implements Serializable {

    private List<String> mobiles;

    private String sendDate;

    public SmsPayrollNotifyDto() {
    }

    public SmsPayrollNotifyDto(List<String> mobiles, String sendDate) {
        this.mobiles = mobiles;
        this.sendDate = sendDate;
    }

    public List<String> getMobiles() {
        return mobiles;
    }

    public void setMobiles(List<String> mobiles) {
        this.mobiles = mobiles;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }
}
