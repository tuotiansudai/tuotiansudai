package com.tuotiansudai.api.service.v1_0;


import com.tuotiansudai.api.dto.v1_0.*;

public interface MobileAppTransferService {

    BaseResponseDto<TransferTransfereeResponseDataDto> getTransferee(TransferTransfereeRequestDto transferTransfereeRequestDto);

    BaseResponseDto<BankAsynResponseDto> transferPurchase(TransferPurchaseRequestDto transferPurchaseRequestDto);

}
