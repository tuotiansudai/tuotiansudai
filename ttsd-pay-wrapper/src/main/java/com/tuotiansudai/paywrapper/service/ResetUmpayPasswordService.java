package com.tuotiansudai.paywrapper.service;


import com.tuotiansudai.dto.ResetUmpayPasswordDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;

public interface ResetUmpayPasswordService {

    BaseDto<PayDataDto> resetUmpayPassword(ResetUmpayPasswordDto resetUmpayPasswordDto);
}
