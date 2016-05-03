package com.tuotiansudai.api.service.v1_0.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.MobileAppWithdrawService;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.WithdrawDto;
import com.tuotiansudai.repository.mapper.BankCardMapper;
import com.tuotiansudai.repository.mapper.WithdrawMapper;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.repository.model.WithdrawModel;
import com.tuotiansudai.util.AmountConverter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public class MobileAppWithdrawServiceImpl implements MobileAppWithdrawService {
    static Logger logger = Logger.getLogger(MobileAppWithdrawServiceImpl.class);
    @Autowired
    private BankCardMapper bankCardMapper;
    @Autowired
    private PayWrapperClient payWrapperClient;
    @Autowired
    private WithdrawMapper withdrawMapper;
    @Value("${pay.withdraw.fee}")
    private long withdrawFee;

    @Override
    public BaseResponseDto queryUserWithdrawLogs(WithdrawListRequestDto requestDto) {
        String loginName = requestDto.getBaseParam().getUserId();
        Integer index = requestDto.getIndex();
        Integer pageSize = requestDto.getPageSize();

        if (index == null || index <= 0) {
            index = 1;
        }
        if (pageSize == null || pageSize <= 0) {
            pageSize = 10;
        }
        long count = withdrawMapper.findWithdrawCount(null, loginName, null, null, null, null);
        List<WithdrawModel> withdrawModels = withdrawMapper.findWithdrawPagination(null, loginName, null, null, (index - 1) * pageSize, pageSize, null, null);

        List<WithdrawDetailResponseDataDto> withdrawDetailResponseDataDtos = Lists.transform(withdrawModels, new Function<WithdrawModel, WithdrawDetailResponseDataDto>() {
            @Override
            public WithdrawDetailResponseDataDto apply(WithdrawModel input) {
                return new WithdrawDetailResponseDataDto(input);
            }
        });

        WithdrawListResponseDataDto listDataDto = new WithdrawListResponseDataDto();
        listDataDto.setIndex(index);
        listDataDto.setPageSize(pageSize);
        listDataDto.setTotalCount(count);
        listDataDto.setWithdrawList(withdrawDetailResponseDataDtos);

        BaseResponseDto<WithdrawListResponseDataDto> dto = new BaseResponseDto<>();
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(listDataDto);

        return dto;


    }

    @Override
    public BaseResponseDto generateWithdrawRequest(WithdrawOperateRequestDto requestDto) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        WithdrawDto withdrawDto = requestDto.convertToWithdrawDto();
        long withdrawAmount = AmountConverter.convertStringToCent(withdrawDto.getAmount());
        if (withdrawAmount <= withdrawFee) {
            return new BaseResponseDto(ReturnMessage.WITHDRAW_AMOUNT_NOT_REACH_FEE.getCode(), ReturnMessage.WITHDRAW_AMOUNT_NOT_REACH_FEE.getMsg());
        }
        String loginName = withdrawDto.getLoginName();
        BankCardModel bankCardModel = bankCardMapper.findPassedBankCardByLoginName(loginName);
        if (bankCardModel == null) {
            return new BaseResponseDto(ReturnMessage.NOT_BIND_CARD.getCode(), ReturnMessage.NOT_BIND_CARD.getMsg());
        }
        BaseDto<PayFormDataDto> formDto = payWrapperClient.withdraw(withdrawDto);
        WithdrawOperateResponseDataDto responseDataDto = new WithdrawOperateResponseDataDto();
        try {
            if (formDto.isSuccess()) {
                responseDataDto.setUrl(formDto.getData().getUrl());
                responseDataDto.setRequestData(CommonUtils.mapToFormData(formDto.getData().getFields(), true));
            }
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getLocalizedMessage(), e);
            return new BaseResponseDto(ReturnMessage.UMPAY_INVEST_MESSAGE_INVALID.getCode(), ReturnMessage.UMPAY_INVEST_MESSAGE_INVALID.getMsg());
        }
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        baseResponseDto.setData(responseDataDto);
        return baseResponseDto;
    }
}
