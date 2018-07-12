package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppWithdrawService;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.api.util.PageValidUtils;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.WithdrawDto;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.mapper.BankWithdrawMapper;
import com.tuotiansudai.repository.mapper.UserBankCardMapper;
import com.tuotiansudai.repository.model.UserBankCardModel;
import com.tuotiansudai.repository.model.WithdrawPaginationView;
import com.tuotiansudai.service.BlacklistService;
import com.tuotiansudai.util.AmountConverter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MobileAppWithdrawServiceImpl implements MobileAppWithdrawService {
    static Logger logger = Logger.getLogger(MobileAppWithdrawServiceImpl.class);
    @Autowired
    private UserBankCardMapper userBankCardMapper;
    @Autowired
    private PayWrapperClient payWrapperClient;
    @Autowired
    private BankWithdrawMapper bankWithdrawMapper;
    @Autowired
    private BlacklistService blacklistService;
    @Value("${pay.withdraw.fee}")
    private long withdrawFee;

    @Autowired
    private PageValidUtils pageValidUtils;

    @Override
    public BaseResponseDto<WithdrawListResponseDataDto> queryUserWithdrawLogs(WithdrawListRequestDto requestDto) {
        Integer index = requestDto.getIndex();
        Integer pageSize = pageValidUtils.validPageSizeLimit(requestDto.getPageSize());

        if (index == null || index <= 0) {
            index = 1;
        }

        long count = bankWithdrawMapper.findWithdrawCount(null, requestDto.getBaseParam().getPhoneNum(), null, null, null, null);
        List<WithdrawPaginationView> views = bankWithdrawMapper.findWithdrawPagination(null,
                requestDto.getBaseParam().getPhoneNum(), null, null, (index - 1) * pageSize, 10, null, null);

        List<WithdrawDetailResponseDataDto> withdrawDetailResponseDataDtos = views.stream().map(view -> new WithdrawDetailResponseDataDto(view.getId(), view.getAmount(), view.getStatus(), view.getCreatedTime(), view.getUpdatedTime())).collect(Collectors.toList());

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
        String loginName = withdrawDto.getLoginName();
        if (blacklistService.userIsInBlacklist(loginName)) {
            return new BaseResponseDto(ReturnMessage.WITHDRAW_IN_BLACKLIST.getCode(), ReturnMessage.WITHDRAW_IN_BLACKLIST.getMsg());
        }
        long withdrawAmount = AmountConverter.convertStringToCent(withdrawDto.getAmount());
        if (withdrawAmount <= withdrawFee) {
            return new BaseResponseDto(ReturnMessage.WITHDRAW_AMOUNT_NOT_REACH_FEE.getCode(), ReturnMessage.WITHDRAW_AMOUNT_NOT_REACH_FEE.getMsg());
        }
        UserBankCardModel userBankCardModel = userBankCardMapper.findByLoginNameAndRole(loginName, Role.INVESTOR);
        if (userBankCardModel == null) {
            return new BaseResponseDto(ReturnMessage.NOT_BIND_CARD.getCode(), ReturnMessage.NOT_BIND_CARD.getMsg());
        }
        BaseDto<PayFormDataDto> formDto = payWrapperClient.withdraw(withdrawDto);
        WithdrawOperateResponseDataDto responseDataDto = new WithdrawOperateResponseDataDto();
        try {
            if (formDto.isSuccess()) {
                responseDataDto.setUrl(formDto.getData().getUrl());
                responseDataDto.setRequestData(CommonUtils.mapToFormData(formDto.getData().getFields()));
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
