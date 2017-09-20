package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.Source;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

public class HuiZuActivateAccountDto implements Serializable {

    @NotEmpty
    private String mobile;

    private boolean noPassword;

    private Source source;


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public boolean isNoPassword() {
        return noPassword;
    }

    public void setNoPassword(boolean noPassword) {
        this.noPassword = noPassword;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }
}
