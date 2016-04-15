package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.*;

public interface MobileAppTransferApplicationService {

    BaseResponseDto generateTransferApplication(TransferApplicationRequestDto requestDto);

    BaseResponseDto transferApply(TransferApplyRequestDto requestDto);

    BaseResponseDto transferApplyQuery(TransferApplyQueryRequestDto requestDto);

    BaseResponseDto generateTransfereeApplication(PaginationRequestDto requestDto);

    BaseResponseDto transferApplicationList(TransferApplicationListRequestDto requestDto);

}
