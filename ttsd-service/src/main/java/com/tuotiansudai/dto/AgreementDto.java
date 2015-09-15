package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.AgreementType;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/9/15.
 */
public class AgreementDto implements Serializable {

    private String loginName;

    private AgreementType agreementType;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public AgreementType getAgreementType() {
        return agreementType;
    }

    public void setAgreementType(AgreementType agreementType) {
        this.agreementType = agreementType;
    }

}
