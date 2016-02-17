package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.AgreementOperateRequestDto;
import com.tuotiansudai.api.dto.BaseResponseDto;

public interface MobileAppAgreementService {

    BaseResponseDto generateAgreementRequest(AgreementOperateRequestDto requestDto);

}
