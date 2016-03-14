package com.tuotiansudai.service;


import com.tuotiansudai.dto.AgreementDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;

public interface NoPasswordInvestService {
    void enabledNoPasswordInvest(String loginName);

    void disabledNoPasswordInvest(String loginName);

    BaseDto<PayFormDataDto> agreement(String loginName, AgreementDto agreementDto);

    void writeRemindFlag(String loginName);

}
