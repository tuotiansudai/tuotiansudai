package com.tuotiansudai.dto.sms;

import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

public class PlatformBalanceLowNotifyDto implements Serializable {

    @NotEmpty
    private List<String> mobiles;

    @NotEmpty
    private Double warningLine;

    public List<String> getMobiles() {
        return mobiles;
    }

    public void setMobiles(List<String> mobiles) {
        this.mobiles = mobiles;
    }

    public Double getWarningLine() {
        return warningLine;
    }

    public void setWarningLine(Double warningLine) {
        this.warningLine = warningLine;
    }
}
