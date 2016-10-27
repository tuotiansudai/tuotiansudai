package com.tuotiansudai.dto.sms;

import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

public class PlatformBalanceLowNotifyDto implements Serializable {

    @NotEmpty
    private List<String> mobiles;

    @NotEmpty
    private String warningLine;

    public List<String> getMobiles() {
        return mobiles;
    }

    public void setMobiles(List<String> mobiles) {
        this.mobiles = mobiles;
    }

    public String getWarningLine() {
        return warningLine;
    }

    public void setWarningLine(String warningLine) {
        this.warningLine = warningLine;
    }
}
