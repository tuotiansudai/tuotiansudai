package com.tuotiansudai.api.service.v1_0.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppChannelService;
import com.tuotiansudai.api.service.v1_0.MobileAppRechargeService;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.RechargeDto;
import com.tuotiansudai.repository.mapper.BankCardMapper;
import com.tuotiansudai.repository.mapper.BankMapper;
import com.tuotiansudai.repository.mapper.RechargeMapper;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.repository.model.BankModel;
import com.tuotiansudai.repository.model.RechargeStatus;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

@Service
public class MobileAppRechargeServiceImpl implements MobileAppRechargeService {
    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private MobileAppChannelService mobileAppChannelService;

    @Autowired
    private BankMapper bankMapper;

    @Autowired
    private RechargeMapper rechargeMapper;

    @Override
    public BaseResponseDto recharge(BankCardRequestDto bankCardRequestDto) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        RechargeDto rechargeDto = bankCardRequestDto.convertToRechargeDto();
        rechargeDto.setChannel(mobileAppChannelService.obtainChannelBySource(bankCardRequestDto.getBaseParam()));

        String loginName = rechargeDto.getLoginName();
        BankCardModel bankCardModel = bankCardMapper.findByLoginNameAndIsFastPayOn(loginName);
        if (bankCardModel == null) {
            return new BaseResponseDto(ReturnMessage.FAST_PAY_OFF.getCode(), ReturnMessage.FAST_PAY_OFF.getMsg());
        }
        rechargeDto.setBankCode(bankCardModel.getBankCode());
        BankCardResponseDto bankCardResponseDto = new BankCardResponseDto();
        try {
            BaseDto<PayFormDataDto> formDto = payWrapperClient.recharge(rechargeDto);
            if (formDto.getData().getStatus()) {
                bankCardResponseDto.setUrl(formDto.getData().getUrl());
                bankCardResponseDto.setRequestData(CommonUtils.mapToFormData(formDto.getData().getFields(), true));

            }
        } catch (UnsupportedEncodingException e) {
            return new BaseResponseDto(ReturnMessage.UMPAY_INVEST_MESSAGE_INVALID.getCode(), ReturnMessage.UMPAY_INVEST_MESSAGE_INVALID.getMsg());
        }
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        baseResponseDto.setData(bankCardResponseDto);

        return baseResponseDto;
    }

    private long getLeftRechargeAmount(String mobile, BankModel bankModel) {
        long rechargeAmount = rechargeMapper.findSumRechargeAmount(null, mobile, null, RechargeStatus.SUCCESS, null, null, DateTime.now().withTimeAtStartOfDay().toDate(), new Date());
        return bankModel.getSingleDayAmount() - rechargeAmount;
    }

    @Override
    public BaseResponseDto<BankLimitResponseDataDto> getBankLimit(final BankLimitRequestDto bankLimitRequestDto) {
        String bankCode = bankLimitRequestDto.getBankCode();
        BankLimitResponseDataDto bankLimitResponseDataDto = new BankLimitResponseDataDto();
        if (StringUtils.isEmpty(bankCode)) {
            List<BankModel> bankModelList = bankMapper.findBankList();
            List<BankLimitUnitDto> bankLimitUnitDtos = Lists.transform(bankModelList, new Function<BankModel, BankLimitUnitDto>() {
                @Override
                public BankLimitUnitDto apply(BankModel bankModel) {
                    return new BankLimitUnitDto(AmountConverter.convertCentToString(bankModel.getSingleAmount()),
                            AmountConverter.convertCentToString(bankModel.getSingleDayAmount()), bankModel.getBankCode());
                }
            });

            bankLimitResponseDataDto.setRechargeLeftAmount("");
            bankLimitResponseDataDto.setBankLimits(bankLimitUnitDtos);
        } else {
            BankModel bankModel = bankMapper.findByBankCode(bankCode);
            if (null == bankModel) {
                return new BaseResponseDto<>(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode(), ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
            }
            long leftAmount = getLeftRechargeAmount(bankLimitRequestDto.getBaseParam().getPhoneNum(), bankModel);
            if (leftAmount < 0) {
                return new BaseResponseDto<>(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode(), ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
            } else {
                bankLimitResponseDataDto.setRechargeLeftAmount(AmountConverter.convertCentToString(leftAmount));
                bankLimitResponseDataDto.setBankLimits(Lists.newArrayList(new BankLimitUnitDto(AmountConverter.convertCentToString(bankModel.getSingleAmount()),
                        AmountConverter.convertCentToString(bankModel.getSingleDayAmount()), bankModel.getBankCode())));
            }
        }
        BaseResponseDto<BankLimitResponseDataDto> bankLimitResponseDto = new BaseResponseDto<>(ReturnMessage.SUCCESS.getCode(), ReturnMessage.SUCCESS.getMsg());
        bankLimitResponseDto.setData(bankLimitResponseDataDto);
        return bankLimitResponseDto;
    }
}
