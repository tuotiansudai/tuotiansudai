package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;

public interface AnxinSignService {

    BaseDto<BaseDataDto> createAccount3001(String loginName);

    BaseDto<BaseDataDto> sendCaptcha3101(String loginName, boolean isVoice);

    BaseDto<BaseDataDto> verifyCaptcha3102(String loginName, String captcha, boolean isSkipAuth);

    BaseDto<BaseDataDto> createContracts(long loanId);

}
