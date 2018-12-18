package com.tuotiansudai.api.service.v1_0.impl;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.PersonalInfoRequestDto;
import com.tuotiansudai.api.dto.v1_0.PersonalInfoResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppPersonalInfoService;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.api.util.DistrictUtil;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.BankCardUtil;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MobileAppPersonalInfoServiceImpl implements MobileAppPersonalInfoService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private AnxinSignPropertyMapper anxinSignPropertyMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private RiskEstimateMapper riskEstimateMapper;

    @Value("${risk.estimate.limit.key}")
    private String riskEstimateLimitKey;

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Override
    public BaseResponseDto<PersonalInfoResponseDataDto> getPersonalInfoData(PersonalInfoRequestDto personalInfoRequestDto) {
        BaseResponseDto<PersonalInfoResponseDataDto> dto = new BaseResponseDto<>();
        String loginName = personalInfoRequestDto.getUserName();

        UserModel userModel = userMapper.findByLoginNameOrMobile(loginName);

        if (userModel == null) {
            dto.setCode(ReturnMessage.USER_ID_IS_NULL.getCode());
            dto.setMessage(ReturnMessage.USER_ID_IS_NULL.getMsg());
        } else {
            BankCardModel bankCard = bankCardMapper.findPassedBankCardByLoginName(loginName);
            AccountModel account = accountService.findByLoginName(loginName);
            AnxinSignPropertyModel anxinProp = anxinSignPropertyMapper.findByLoginName(loginName);
            PersonalInfoResponseDataDto personalInfoDataDto = generatePersonalInfoData(userModel, bankCard, account, anxinProp);

            dto.setData(personalInfoDataDto);
            dto.setCode(ReturnMessage.SUCCESS.getCode());
            dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        }
        return dto;
    }

    private PersonalInfoResponseDataDto generatePersonalInfoData(UserModel user, BankCardModel bankCard, AccountModel account, AnxinSignPropertyModel anxinProp) {
        PersonalInfoResponseDataDto personalInfoDataDto = new PersonalInfoResponseDataDto();
        personalInfoDataDto.setUserId(user.getLoginName());
        personalInfoDataDto.setUserName(user.getMobile());
        personalInfoDataDto.setPhoneNum(user.getMobile());
        personalInfoDataDto.setEmail(user.getEmail());
        if (StringUtils.isNotEmpty(user.getProvince())) {
            personalInfoDataDto.setDistrictCode(DistrictUtil.convertNameToCode(user.getProvince()));
        }
        if (account != null) {
            personalInfoDataDto.setCertificationFlag(true);
            personalInfoDataDto.setRealName(user.getUserName());
            personalInfoDataDto.setIdCard(user.getIdentityNumber());
            personalInfoDataDto.setAutoInvest(account.isAutoInvest());
        } else {
            personalInfoDataDto.setCertificationFlag(false);
            personalInfoDataDto.setRealName("");
            personalInfoDataDto.setIdCard("");
            personalInfoDataDto.setAutoInvest(false);
        }
        if (bankCard != null) {
            personalInfoDataDto.setIsBoundBankCard(true);
            personalInfoDataDto.setBankCardNo(CommonUtils.encryptBankCardNo(bankCard.getCardNumber()));
            personalInfoDataDto.setBankId(bankCard.getBankCode());
            personalInfoDataDto.setIsFastPayment(bankCard.isFastPayOn());
            personalInfoDataDto.setFastPaymentEnable(BankCardUtil.canEnableFastPay(bankCard.getBankCode()));
            personalInfoDataDto.setBankName(BankCardUtil.getBankName(bankCard.getBankCode()));
        } else {
            personalInfoDataDto.setIsBoundBankCard(false);
            personalInfoDataDto.setBankCardNo("");
            personalInfoDataDto.setBankId("");
            personalInfoDataDto.setIsFastPayment(false);
            personalInfoDataDto.setFastPaymentEnable(false);
            personalInfoDataDto.setBankName("");
        }

        if (anxinProp != null) {
            personalInfoDataDto.setAnxinUser(StringUtils.isNotEmpty(anxinProp.getAnxinUserId()));
            personalInfoDataDto.setSkipAuth(anxinProp.isSkipAuth());
            personalInfoDataDto.setHasAuthed(StringUtils.isNotEmpty(anxinProp.getProjectCode()));
        } else {
            personalInfoDataDto.setAnxinUser(false);
            personalInfoDataDto.setSkipAuth(false);
            personalInfoDataDto.setHasAuthed(false);
        }

        personalInfoDataDto.setIsExperienceEnable(CollectionUtils.isNotEmpty(userCouponMapper.findUsedExperienceByLoginName(user.getLoginName())));
        personalInfoDataDto.setIsNewbieEnable(investMapper.sumSuccessInvestCountByLoginName(user.getLoginName()) == 0);
        RiskEstimateModel riskEstimateModel = riskEstimateMapper.findByLoginName(user.getLoginName());
        personalInfoDataDto.setRiskEstimate(riskEstimateModel != null ? riskEstimateModel.getEstimate().getType() : "");
        personalInfoDataDto.setRiskEstimateDesc(riskEstimateModel != null ? riskEstimateModel.getEstimate().getDescription() : "");
        personalInfoDataDto.setEstimateLevel(riskEstimateModel != null ? riskEstimateModel.getEstimate().getLower() : 0);
        personalInfoDataDto.setEstimateLimit(riskEstimateModel != null ? AmountConverter.convertStringToCent(redisWrapperClient.hget(riskEstimateLimitKey,riskEstimateModel.getEstimate().name())) : 0);
        return personalInfoDataDto;
    }
}
