package com.tuotiansudai.api.service.impl;

import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppWithdrawService;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.WithdrawDto;
import com.tuotiansudai.repository.mapper.BankCardMapper;
import com.tuotiansudai.repository.model.BankCardModel;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class MobileAppWithdrawServiceImpl implements MobileAppWithdrawService {
    static Logger logger = Logger.getLogger(MobileAppWithdrawServiceImpl.class);
    @Autowired
    private BankCardMapper bankCardMapper;
    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public BaseResponseDto queryUserWithdrawLogs(WithdrawListRequestDto requestDto) {
        throw new NotImplementedException(getClass().getName());
    }

    @Override
    public BaseResponseDto generateWithdrawRequest(WithdrawOperateRequestDto requestDto) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        WithdrawDto withdrawDto = requestDto.convertToWithdrawDto();
        String loginName = withdrawDto.getLoginName();
        BankCardModel bankCardModel = bankCardMapper.findByLoginName(loginName);
        if(bankCardModel == null){
            return new BaseResponseDto(ReturnMessage.NOT_BIND_CARD.getCode(),ReturnMessage.NOT_BIND_CARD.getMsg());
        }
        BaseDto<PayFormDataDto> formDto = payWrapperClient.withdraw(withdrawDto);
        WithdrawOperateResponseDataDto responseDataDto = new WithdrawOperateResponseDataDto();
        try {
            if(formDto.isSuccess()){
                responseDataDto.setUrl(formDto.getData().getUrl());
                responseDataDto.setRequestData(CommonUtils.mapToFormData(formDto.getData().getFields(), true));
            }
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getLocalizedMessage(),e);
            return new BaseResponseDto(ReturnMessage.UMPAY_INVEST_MESSAGE_INVALID.getCode(), ReturnMessage.UMPAY_INVEST_MESSAGE_INVALID.getMsg());
        }
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        baseResponseDto.setData(responseDataDto);
        return baseResponseDto;
    }
}
