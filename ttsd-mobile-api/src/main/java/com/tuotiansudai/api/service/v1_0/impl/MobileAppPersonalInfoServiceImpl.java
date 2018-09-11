package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.PersonalInfoResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppPersonalInfoService;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.api.util.DistrictUtil;
import com.tuotiansudai.enums.RechargeStatus;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.BankAccountService;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MobileAppPersonalInfoServiceImpl implements MobileAppPersonalInfoService {

    private final UserMapper userMapper;

    private final UserBankCardMapper userBankCardMapper;

    private final BankAccountService bankAccountService;

    private final InvestMapper investMapper;

    private final AnxinSignPropertyMapper anxinSignPropertyMapper;

    private final UserCouponMapper userCouponMapper;

    private final RiskEstimateMapper riskEstimateMapper;

    private final BankMapper bankMapper;

    private final BankRechargeMapper bankRechargeMapper;

    private final AccountMapper accountMapper;

    @Autowired
    public MobileAppPersonalInfoServiceImpl(UserMapper userMapper, UserBankCardMapper userBankCardMapper, BankAccountService bankAccountService, InvestMapper investMapper, AnxinSignPropertyMapper anxinSignPropertyMapper, UserCouponMapper userCouponMapper, RiskEstimateMapper riskEstimateMapper, BankMapper bankMapper, BankRechargeMapper bankRechargeMapper, AccountMapper accountMapper) {
        this.userMapper = userMapper;
        this.userBankCardMapper = userBankCardMapper;
        this.bankAccountService = bankAccountService;
        this.investMapper = investMapper;
        this.anxinSignPropertyMapper = anxinSignPropertyMapper;
        this.userCouponMapper = userCouponMapper;
        this.riskEstimateMapper = riskEstimateMapper;
        this.bankMapper = bankMapper;
        this.bankRechargeMapper = bankRechargeMapper;
        this.accountMapper = accountMapper;
    }

    @Override
    public BaseResponseDto<PersonalInfoResponseDataDto> getPersonalInfoData(String loginName) {
        BaseResponseDto<PersonalInfoResponseDataDto> dto = new BaseResponseDto<>();

        UserModel userModel = userMapper.findByLoginName(loginName);
        UserBankCardModel userBankCardModel = userBankCardMapper.findByLoginNameAndRole(loginName, Role.INVESTOR);
        BankAccountModel account = bankAccountService.findBankAccount(loginName, Role.INVESTOR);
        AnxinSignPropertyModel anxinProp = anxinSignPropertyMapper.findByLoginName(loginName);
        PersonalInfoResponseDataDto personalInfoDataDto = new PersonalInfoResponseDataDto();

        personalInfoDataDto.setUserId(userModel.getLoginName());
        personalInfoDataDto.setUserName(userModel.getMobile());
        personalInfoDataDto.setPhoneNum(userModel.getMobile());
        personalInfoDataDto.setEmail(userModel.getEmail());
        personalInfoDataDto.setDistrictCode(DistrictUtil.convertNameToCode(userModel.getProvince()));
        personalInfoDataDto.setHasUmpAccount(accountMapper.findByLoginName(userModel.getLoginName()) != null);
        if (account != null) {
            personalInfoDataDto.setCertificationFlag(true);
            personalInfoDataDto.setRealName(userModel.getUserName());
            personalInfoDataDto.setIdCard(userModel.getIdentityNumber());
            personalInfoDataDto.setAutoInvest(account.isAutoInvest());
        }
        if (userBankCardModel != null) {
            personalInfoDataDto.setIsBoundBankCard(true);
            personalInfoDataDto.setBankCardNo(CommonUtils.encryptBankCardNo(userBankCardModel.getCardNumber()));
            personalInfoDataDto.setBankId(userBankCardModel.getBankCode());
            personalInfoDataDto.setFastPaymentEnable(true);
            personalInfoDataDto.setBankName(userBankCardModel.getBank());
            BankModel bankModel = bankMapper.findByBankCode(userBankCardModel.getBankCode());
            long rechargeAmount = bankRechargeMapper.findSumRechargeAmount(null, null, userModel.getMobile(), null, RechargeStatus.SUCCESS, null, DateTime.now().withTimeAtStartOfDay().toDate(), new Date());
            personalInfoDataDto.setSingleAmount(bankModel == null ? "0" : AmountConverter.convertCentToString(bankModel.getSingleAmount()));
            personalInfoDataDto.setSingleDayAmount(bankModel == null ? "0" : AmountConverter.convertCentToString(bankModel.getSingleDayAmount()));
            personalInfoDataDto.setRechargeLeftAmount(bankModel == null ? "0" : AmountConverter.convertCentToString(bankModel.getSingleDayAmount() - rechargeAmount));
        }
        if (anxinProp != null) {
            personalInfoDataDto.setAnxinUser(StringUtils.isNotEmpty(anxinProp.getAnxinUserId()));
            personalInfoDataDto.setSkipAuth(anxinProp.isSkipAuth());
            personalInfoDataDto.setHasAuthed(StringUtils.isNotEmpty(anxinProp.getProjectCode()));
        }

        personalInfoDataDto.setIsExperienceEnable(CollectionUtils.isNotEmpty(userCouponMapper.findUsedExperienceByLoginName(userModel.getLoginName())));
        personalInfoDataDto.setIsNewbieEnable(investMapper.sumSuccessInvestCountByLoginName(userModel.getLoginName()) == 0);
        RiskEstimateModel riskEstimateModel = riskEstimateMapper.findByLoginName(userModel.getLoginName());
        personalInfoDataDto.setRiskEstimate(riskEstimateModel != null ? riskEstimateModel.getEstimate().getType() : "");
        personalInfoDataDto.setRiskEstimateDesc(riskEstimateModel != null ? riskEstimateModel.getEstimate().getDescription() : "");

        dto.setData(personalInfoDataDto);
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return dto;
    }
}
