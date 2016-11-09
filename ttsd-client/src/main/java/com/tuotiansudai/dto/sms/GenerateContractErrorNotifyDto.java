package com.tuotiansudai.dto.sms;

import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

public class GenerateContractErrorNotifyDto implements Serializable {

    @NotEmpty
    private List<String> mobiles;

    @NotEmpty
    private List<String> warningLine;

    public GenerateContractErrorNotifyDto() {
    }

    public GenerateContractErrorNotifyDto(List<String> mobiles, List<String> warningLine) {
        this.mobiles = mobiles;
        this.warningLine = warningLine;
    }

    public List<String> getMobiles() {
        return mobiles;
    }

    public void setMobiles(List<String> mobiles) {
        this.mobiles = mobiles;
    }

    public List<String> getWarningLine() {
        return warningLine;
    }

    public void setWarningLine(List<String> warningLine) {
        this.warningLine = warningLine;
    }
}
