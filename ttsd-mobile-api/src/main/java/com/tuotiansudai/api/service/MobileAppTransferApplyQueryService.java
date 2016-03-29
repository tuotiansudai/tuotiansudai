package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.TransferApplyQueryRequestDto;

public interface MobileAppTransferApplyQueryService {

    BaseResponseDto transferApplyQuery(TransferApplyQueryRequestDto requestDto);

}
