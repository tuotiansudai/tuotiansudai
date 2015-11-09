package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.RegisterAccountDto;

public interface RegisterService {

    BaseDto register(RegisterAccountDto dto);

}
