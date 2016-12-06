package com.tuotiansudai.api.service.v1_0;


import com.tuotiansudai.api.dto.v1_0.*;

public interface MobileAppTransferApplicationService {

    BaseResponseDto<TransferApplicationResponseDataDto> generateTransferApplication(TransferApplicationRequestDto requestDto);

    BaseResponseDto transferApply(TransferApplyRequestDto requestDto);

    BaseResponseDto<TransferApplyQueryResponseDataDto> transferApplyQuery(TransferApplyQueryRequestDto requestDto);

    BaseResponseDto<TransferApplicationResponseDataDto> generateTransfereeApplication(PaginationRequestDto requestDto);

    BaseResponseDto transferApplicationCancel(TransferCancelRequestDto transferCancelRequestDto);

    BaseResponseDto<TransferPurchaseResponseDataDto> transferPurchase(TransferPurchaseRequestDto requestDto);

    BaseResponseDto<TransferApplicationResponseDataDto> transferApplicationList(TransferApplicationListRequestDto requestDto);

    BaseResponseDto<TransferApplicationDetailResponseDataDto> transferApplicationById(TransferApplicationDetailRequestDto requestDto);

    BaseResponseDto<UserInvestRepayResponseDataDto> userInvestRepay(UserInvestRepayRequestDto userInvestRepayRequestDto);
}
