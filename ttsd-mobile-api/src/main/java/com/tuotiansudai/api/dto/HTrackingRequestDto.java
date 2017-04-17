package com.tuotiansudai.api.dto;

import java.util.List;

public class HTrackingRequestDto{

    private List<HTrackingRequestInfoDto> regs;

    public List<HTrackingRequestInfoDto> getRegs() {
        return regs;
    }

    public void setRegs(List<HTrackingRequestInfoDto> regs) {
        this.regs = regs;
    }
}
