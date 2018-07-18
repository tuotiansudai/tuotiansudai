package com.tuotiansudai.api.service.v1_0.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppRechargeService;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.enums.BankRechargeStatus;
import com.tuotiansudai.fudian.dto.RechargePayType;
import com.tuotiansudai.fudian.message.BankAsyncMessage;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.mapper.BankMapper;
import com.tuotiansudai.repository.mapper.BankRechargeMapper;
import com.tuotiansudai.repository.mapper.UserBankCardMapper;
import com.tuotiansudai.repository.model.BankModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.BankRechargeService;
import com.tuotiansudai.util.AmountConverter;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class MobileAppRechargeServiceImpl implements MobileAppRechargeService {

    final static Logger logger = Logger.getLogger(MobileAppRechargeServiceImpl.class);

    @Autowired
    private BankMapper bankMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserBankCardMapper userBankCardMapper;

    @Autowired
    private BankRechargeMapper rechargeMapper;

    @Autowired
    private BankRechargeService bankRechargeService;

    @Override
    public BaseResponseDto<BankAsynResponseDto> recharge(String loginName, BankRechargeRequestDto bankRechargeRequestDto) {
        if (userBankCardMapper.findByLoginNameAndRole(loginName, Role.INVESTOR) == null) {
            return new BaseResponseDto<>(ReturnMessage.BANK_CARD_NOT_BOUND);
        }
        UserModel userModel = userMapper.findByLoginName(loginName);
        BankAsyncMessage bankAsyncMessage = bankRechargeService.recharge(
                Source.valueOf(bankRechargeRequestDto.getBaseParam().getPlatform().toUpperCase()),
                userModel.getLoginName(),
                userModel.getMobile(),
                AmountConverter.convertStringToCent(bankRechargeRequestDto.getAmount()),
                RechargePayType.FAST_PAY.name(),
                bankRechargeRequestDto.getBaseParam().getChannel(), Role.INVESTOR);
        return CommonUtils.mapToFormData(bankAsyncMessage);
    }

    private long getLeftRechargeAmount(String mobile, BankModel bankModel) {
        long rechargeAmount = rechargeMapper.findSumRechargeAmount(null,null, mobile, null, BankRechargeStatus.SUCCESS,  null, DateTime.now().withTimeAtStartOfDay().toDate(), new Date());
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
