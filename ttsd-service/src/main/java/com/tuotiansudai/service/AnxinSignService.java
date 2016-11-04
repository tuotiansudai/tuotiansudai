package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.repository.model.AnxinSignPropertyModel;

public interface AnxinSignService {

    boolean hasAuthedBefore(String loginName);

    AnxinSignPropertyModel getAnxinSignProp(String loginName);

    BaseDto<BaseDataDto> createAccount3001(String loginName);

    BaseDto<BaseDataDto> sendCaptcha3101(String loginName, boolean isVoice);

    BaseDto<BaseDataDto> verifyCaptcha3102(String loginName, String captcha, boolean isSkipAuth, String ip);

    BaseDto<BaseDataDto> createContracts(long loanId);

    BaseDto switchSkipAuth(String loginName, boolean open);

}
