package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.TransferPurchaseRequestDto;
import com.tuotiansudai.api.dto.TransferTransfereeRequestDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;

public interface MobileAppTransferService {

    BaseResponseDto getTransferee(TransferTransfereeRequestDto transferTransfereeRequestDto);

    BaseResponseDto transferPurchase(TransferPurchaseRequestDto transferPurchaseRequestDto);

    BaseResponseDto transferNoPasswordPurchase(TransferPurchaseRequestDto transferPurchaseRequestDto);

}
