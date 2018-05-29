package com.tuotiansudai.dto.sms;


import com.google.common.collect.Lists;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

public class SmsDto implements Serializable{

    @NotNull
    private JianZhouSmsTemplate jianZhouSmsTemplate;

    @NotEmpty
    private List<String> mobiles;

    private List<String> params;

    private boolean isVoice;

    private String requestIP;

    public SmsDto() {
    }

    public SmsDto(JianZhouSmsTemplate jianZhouSmsTemplate, List<String> mobiles) {
        this.jianZhouSmsTemplate = jianZhouSmsTemplate;
        this.mobiles = mobiles;
        this.params = Lists.newArrayList();
        this.requestIP = null;
    }

    public SmsDto(JianZhouSmsTemplate jianZhouSmsTemplate, List<String> mobiles, List<String> params) {
        this.jianZhouSmsTemplate = jianZhouSmsTemplate;
        this.mobiles = mobiles;
        this.params = params;
        this.requestIP = null;
    }

    public SmsDto(JianZhouSmsTemplate jianZhouSmsTemplate, List<String> mobiles, List<String> params, boolean isVoice, String requestIP) {
        this.jianZhouSmsTemplate = jianZhouSmsTemplate;
        this.mobiles = mobiles;
        this.params = params;
        this.isVoice = isVoice;
        this.requestIP = requestIP;
    }

    public JianZhouSmsTemplate getJianZhouSmsTemplate() {
        return jianZhouSmsTemplate;
    }

    public void setJianZhouSmsTemplate(JianZhouSmsTemplate jianZhouSmsTemplate) {
        this.jianZhouSmsTemplate = jianZhouSmsTemplate;
    }

    public List<String> getMobiles() {
        return mobiles;
    }

    public void setMobiles(List<String> mobiles) {
        this.mobiles = mobiles;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public boolean isVoice() {
        return isVoice;
    }

    public void setVoice(boolean voice) {
        isVoice = voice;
    }

    public String getRequestIP() {
        return requestIP;
    }

    public void setRequestIP(String requestIP) {
        this.requestIP = requestIP;
    }
}
