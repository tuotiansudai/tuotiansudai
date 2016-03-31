package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.TransferApplicationRequestDto;
import com.tuotiansudai.api.dto.TransferApplyQueryRequestDto;
import com.tuotiansudai.api.dto.TransferApplyRequestDto;

public interface MobileAppTransferApplicationService {

    BaseResponseDto generateTransferApplication(TransferApplicationRequestDto requestDto);

    BaseResponseDto transferApply(TransferApplyRequestDto requestDto);

    BaseResponseDto transferApplyQuery(TransferApplyQueryRequestDto requestDto);

}
