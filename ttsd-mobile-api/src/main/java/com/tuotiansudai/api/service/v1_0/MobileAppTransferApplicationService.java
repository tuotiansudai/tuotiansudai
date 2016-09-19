package com.tuotiansudai.api.service.v1_0;


import com.tuotiansudai.api.dto.v1_0.*;

public interface MobileAppTransferApplicationService {

    BaseResponseDto generateTransferApplication(TransferApplicationRequestDto requestDto);

    BaseResponseDto transferApply(TransferApplyRequestDto requestDto);

    BaseResponseDto transferApplyQuery(TransferApplyQueryRequestDto requestDto);

    BaseResponseDto generateTransfereeApplication(PaginationRequestDto requestDto);

    BaseResponseDto transferApplicationCancel(TransferCancelRequestDto transferCancelRequestDto);

    BaseResponseDto transferPurchase(TransferPurchaseRequestDto requestDto);

    BaseResponseDto transferApplicationList(TransferApplicationListRequestDto requestDto);

    BaseResponseDto transferApplicationById(TransferApplicationDetailRequestDto requestDto);

    BaseResponseDto userInvestRepay(UserInvestRepayRequestDto userInvestRepayRequestDto);
}
