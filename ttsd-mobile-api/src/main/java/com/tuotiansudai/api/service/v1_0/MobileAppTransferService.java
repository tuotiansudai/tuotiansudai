package com.tuotiansudai.api.service.v1_0;


import com.tuotiansudai.api.dto.v1_0.TransferPurchaseRequestDto;
import com.tuotiansudai.api.dto.v1_0.TransferTransfereeRequestDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;

public interface MobileAppTransferService {

    BaseResponseDto getTransferee(TransferTransfereeRequestDto transferTransfereeRequestDto);

    BaseResponseDto transferPurchase(TransferPurchaseRequestDto transferPurchaseRequestDto);

    BaseResponseDto transferNoPasswordPurchase(TransferPurchaseRequestDto transferPurchaseRequestDto);

}
