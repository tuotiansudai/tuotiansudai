package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.TransferApplicationRequestDto;

public interface MobileAppTransferApplyService {

    BaseResponseDto transferApply(TransferApplicationRequestDto requestDto);

}
