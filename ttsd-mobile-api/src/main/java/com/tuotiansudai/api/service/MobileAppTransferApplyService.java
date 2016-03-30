package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.TransferApplyRequestDto;

public interface MobileAppTransferApplyService {

    BaseResponseDto transferApply(TransferApplyRequestDto requestDto);

}
