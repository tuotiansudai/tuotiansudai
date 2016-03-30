package com.tuotiansudai.api.service.impl;


import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.dto.TransferApplyRequestDto;
import com.tuotiansudai.api.service.MobileAppTransferApplyService;
import com.tuotiansudai.transfer.dto.TransferApplicationDto;
import com.tuotiansudai.transfer.service.InvestTransferService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileAppTransferApplyServiceImpl implements MobileAppTransferApplyService{

    static Logger logger = Logger.getLogger(MobileAppTransferApplyServiceImpl.class);
    @Autowired
    private InvestTransferService investTransferService;

    @Override
    public BaseResponseDto transferApply(TransferApplyRequestDto requestDto) {
        TransferApplicationDto transferApplicationDto = requestDto.convertToTransferApplicationDto();
        try {
            investTransferService.investTransferApply(transferApplicationDto);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(),e);
            return new BaseResponseDto(ReturnMessage.TRANSFER_APPLY_IS_FAIL.getCode(),ReturnMessage.TRANSFER_APPLY_IS_FAIL.getMsg());
        }

        return new BaseResponseDto(ReturnMessage.SUCCESS.getCode(),ReturnMessage.SUCCESS.getMsg());

    }
}
