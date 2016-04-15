package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.TransferTransfereeRequestDto;

public interface MobileAppTransferService {

    BaseResponseDto getTransferee(TransferTransfereeRequestDto transferTransfereeRequestDto);

}
