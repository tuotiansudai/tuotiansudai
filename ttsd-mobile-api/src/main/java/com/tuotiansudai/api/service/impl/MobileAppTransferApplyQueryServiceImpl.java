package com.tuotiansudai.api.service.impl;


import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.TransferApplyQueryRequestDto;
import com.tuotiansudai.api.dto.TransferApplyQueryResponseDataDto;
import com.tuotiansudai.api.service.MobileAppTransferApplyQueryService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class MobileAppTransferApplyQueryServiceImpl implements MobileAppTransferApplyQueryService {

    static Logger logger = Logger.getLogger(MobileAppTransferApplyQueryServiceImpl.class);


    @Override
    public BaseResponseDto transferApplyQuery(TransferApplyQueryRequestDto requestDto) {
        BaseResponseDto<TransferApplyQueryResponseDataDto> baseResponseDto = new BaseResponseDto<>();
        
        return baseResponseDto;
    }
}
