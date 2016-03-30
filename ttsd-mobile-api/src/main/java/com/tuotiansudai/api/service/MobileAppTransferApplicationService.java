package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.TransferApplicationRequestDto;
import com.tuotiansudai.api.dto.TransferApplyRequestDto;

public interface MobileAppTransferApplicationService {

    BaseResponseDto generateTransferApplication(TransferApplicationRequestDto requestDto);

}
