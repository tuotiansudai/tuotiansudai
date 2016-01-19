package com.tuotiansudai.api.service.impl;


import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.PersonalInfoRequestDto;
import com.tuotiansudai.api.dto.PersonalInfoResponseDataDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppPersonalInfoService;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.api.util.DistrictUtil;
import com.tuotiansudai.repository.mapper.BankCardMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.util.BankCardUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileAppPersonalInfoServiceImpl implements MobileAppPersonalInfoService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private AccountService accountService;

    @Override
    public BaseResponseDto getPersonalInfoData(PersonalInfoRequestDto personalInfoRequestDto) {
        BaseResponseDto<PersonalInfoResponseDataDto> dto = new BaseResponseDto();
        String loginName = personalInfoRequestDto.getUserName();

        UserModel userModel = userMapper.findByLoginNameOrMobile(loginName);

        if (userModel == null) {
            dto.setCode(ReturnMessage.USER_ID_IS_NULL.getCode());
            dto.setMessage(ReturnMessage.USER_ID_IS_NULL.getMsg());
        } else {
            BankCardModel bankCard = bankCardMapper.findPassedBankCardByLoginName(loginName);
            AccountModel account = accountService.findByLoginName(loginName);
            PersonalInfoResponseDataDto personalInfoDataDto = generatePersonalInfoData(userModel, bankCard, account);

            dto.setData(personalInfoDataDto);
            dto.setCode(ReturnMessage.SUCCESS.getCode());
            dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        }
        return dto;
    }

    private PersonalInfoResponseDataDto generatePersonalInfoData(UserModel user, BankCardModel bankCard, AccountModel account) {
        PersonalInfoResponseDataDto personalInfoDataDto = new PersonalInfoResponseDataDto();
        personalInfoDataDto.setUserId(user.getLoginName());
        personalInfoDataDto.setUserName(user.getLoginName());
        personalInfoDataDto.setPhoneNum(user.getMobile());
        personalInfoDataDto.setPhoto(user.getAvatar());
        personalInfoDataDto.setEmail(user.getEmail());
        if(StringUtils.isNotEmpty(user.getProvince())){
            personalInfoDataDto.setDistrictCode(DistrictUtil.convertNameToCode(user.getProvince()));
        }
        if (account != null) {
            personalInfoDataDto.setCertificationFlag(true);
            personalInfoDataDto.setRealName(account.getUserName());
            personalInfoDataDto.setIdCard(account.getIdentityNumber());
        } else {
            personalInfoDataDto.setCertificationFlag(false);
            personalInfoDataDto.setRealName("");
            personalInfoDataDto.setIdCard("");
        }
        if (bankCard != null) {
            personalInfoDataDto.setIsBoundBankCard(true);
            personalInfoDataDto.setBankCardNo(CommonUtils.encryptBankCardNo(bankCard.getCardNumber()));
            personalInfoDataDto.setBankId(bankCard.getBankCode());
            personalInfoDataDto.setIsFastPayment(bankCard.isFastPayOn());
            personalInfoDataDto.setFastPaymentEnable(BankCardUtil.canEnableFastPay(bankCard.getBankCode()));
        } else {
            personalInfoDataDto.setIsBoundBankCard(false);
            personalInfoDataDto.setBankCardNo("");
            personalInfoDataDto.setBankId("");
            personalInfoDataDto.setIsFastPayment(false);
            personalInfoDataDto.setFastPaymentEnable(false);
        }
        return personalInfoDataDto;
    }
}
