package com.tuotiansudai.dto.sms;


import java.io.Serializable;
import java.util.List;

public class SmsMembershipPrivilegeNotifyDto implements Serializable{

    private List<String> mobiles;

    public SmsMembershipPrivilegeNotifyDto() {
    }

    public SmsMembershipPrivilegeNotifyDto(List<String> mobiles) {
        this.mobiles = mobiles;
    }

    public List<String> getMobiles() {
        return mobiles;
    }

    public void setMobiles(List<String> mobiles) {
        this.mobiles = mobiles;
    }
}
