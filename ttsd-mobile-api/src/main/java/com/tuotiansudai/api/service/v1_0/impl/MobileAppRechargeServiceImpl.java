package com.tuotiansudai.api.service.v1_0.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppChannelService;
import com.tuotiansudai.api.service.v1_0.MobileAppRechargeService;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.request.BankRechargeRequestDto;
import com.tuotiansudai.enums.BankRechargeStatus;
import com.tuotiansudai.repository.mapper.BankMapper;
import com.tuotiansudai.repository.mapper.BankRechargeMapper;
import com.tuotiansudai.repository.mapper.UserBankCardMapper;
import com.tuotiansudai.repository.model.BankModel;
import com.tuotiansudai.repository.model.UserBankCardModel;
import com.tuotiansudai.util.AmountConverter;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

@Service
public class MobileAppRechargeServiceImpl implements MobileAppRechargeService {

    final static Logger logger = Logger.getLogger(MobileAppRechargeServiceImpl.class);

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private UserBankCardMapper userBankCardMapper;

    @Autowired
    private MobileAppChannelService mobileAppChannelService;

    @Autowired
    private BankMapper bankMapper;

    @Autowired
    private BankRechargeMapper rechargeMapper;

    @Override
    public BaseResponseDto<BankCardResponseDto> recharge(BankCardRequestDto bankCardRequestDto) {
        BaseResponseDto<BankCardResponseDto> baseResponseDto = new BaseResponseDto<>();
        BankRechargeRequestDto rechargeDto = bankCardRequestDto.convertToRechargeDto();
        rechargeDto.setChannel(mobileAppChannelService.obtainChannelBySource(bankCardRequestDto.getBaseParam()));

        String loginName = rechargeDto.getLoginName();
        UserBankCardModel userBankCardModel = userBankCardMapper.findByLoginName(loginName);
        if (userBankCardModel == null) {
            return new BaseResponseDto<>(ReturnMessage.FAST_PAY_OFF.getCode(), ReturnMessage.FAST_PAY_OFF.getMsg());
        }
        BankCardResponseDto bankCardResponseDto = new BankCardResponseDto();
        try {
            BaseDto<PayFormDataDto> formDto = payWrapperClient.recharge(rechargeDto);
            if (!formDto.isSuccess()) {
                logger.error("[MobileAppRechargeServiceImpl][recharge] pay wrapper may fail to connect.");

                baseResponseDto.setCode(ReturnMessage.FAIL.getCode());
                baseResponseDto.setMessage(ReturnMessage.FAIL.getMsg());
                return baseResponseDto;
            }
            if (formDto.getData().getStatus()) {
                bankCardResponseDto.setUrl(formDto.getData().getUrl());
                bankCardResponseDto.setRequestData(CommonUtils.mapToFormData(formDto.getData().getFields()));
            }
        } catch (UnsupportedEncodingException e) {
            return new BaseResponseDto<>(ReturnMessage.UMPAY_INVEST_MESSAGE_INVALID.getCode(), ReturnMessage.UMPAY_INVEST_MESSAGE_INVALID.getMsg());
        }
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        baseResponseDto.setData(bankCardResponseDto);

        return baseResponseDto;
    }

    private long getLeftRechargeAmount(String mobile, BankModel bankModel) {
        long rechargeAmount = rechargeMapper.findSumRechargeAmount(null, mobile, null, BankRechargeStatus.SUCCESS, null, null, DateTime.now().withTimeAtStartOfDay().toDate(), new Date());
        return bankModel.getSingleDayAmount() - rechargeAmount;
    }

    @Override
    public BaseResponseDto<BankLimitResponseDataDto> getBankLimit(final BankLimitRequestDto bankLimitRequestDto) {
        String bankCode = bankLimitRequestDto.getBankCode();
        BankLimitResponseDataDto bankLimitResponseDataDto = new BankLimitResponseDataDto();
        if (StringUtils.isEmpty(bankCode)) {
            List<BankModel> bankModelList = bankMapper.findBankList(0L, 0L);
            List<BankLimitUnitDto> bankLimitUnitDtos = Lists.transform(bankModelList, bankModel -> new BankLimitUnitDto(AmountConverter.convertCentToString(bankModel.getSingleAmount()),
                    AmountConverter.convertCentToString(bankModel.getSingleDayAmount()), bankModel.getBankCode(),
                    bankModel.getName()));

            bankLimitResponseDataDto.setRechargeLeftAmount("");
            bankLimitResponseDataDto.setBankLimits(bankLimitUnitDtos);
        } else {
            BankModel bankModel = bankMapper.findByBankCode(bankCode);
            if (null == bankModel) {
                return new BaseResponseDto<>(ReturnMessage.BIND_CARD_LIMIT_FAIL.getCode(), ReturnMessage.BIND_CARD_LIMIT_FAIL.getMsg());
            }
            long leftAmount = getLeftRechargeAmount(bankLimitRequestDto.getBaseParam().getPhoneNum(), bankModel);
            if (leftAmount < 0) {
                return new BaseResponseDto<>(ReturnMessage.BANK_CARD_RECHARGE_DAILY_LIMIT.getCode(), ReturnMessage.BANK_CARD_RECHARGE_DAILY_LIMIT.getMsg());
            } else {
                bankLimitResponseDataDto.setRechargeLeftAmount(AmountConverter.convertCentToString(leftAmount));
                bankLimitResponseDataDto.setBankLimits(Lists.newArrayList(new BankLimitUnitDto(AmountConverter.convertCentToString(bankModel.getSingleAmount()),
                        AmountConverter.convertCentToString(bankModel.getSingleDayAmount()), bankModel.getBankCode(), bankModel.getName())));
            }
        }
        BaseResponseDto<BankLimitResponseDataDto> bankLimitResponseDto = new BaseResponseDto<>(ReturnMessage.SUCCESS.getCode(), ReturnMessage.SUCCESS.getMsg());
        bankLimitResponseDto.setData(bankLimitResponseDataDto);
        return bankLimitResponseDto;
    }
}
