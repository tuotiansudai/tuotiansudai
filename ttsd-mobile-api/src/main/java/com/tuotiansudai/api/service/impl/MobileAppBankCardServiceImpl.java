package com.tuotiansudai.api.service.impl;

import com.google.common.base.Strings;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppBankCardService;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.dto.AgreementDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BindBankCardDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.BankCardMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.AgreementService;
import com.tuotiansudai.service.BindBankCardService;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class MobileAppBankCardServiceImpl implements MobileAppBankCardService {

    static Logger log = Logger.getLogger(MobileAppBankCardServiceImpl.class);

    @Autowired
    private BindBankCardService bindBankCardService;

    @Autowired
    private AgreementService agreementService;

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;
    @Override
    public BaseResponseDto bindBankCard(BankCardRequestDto requestDto) {
        BaseResponseDto baseDto = new BaseResponseDto();
        try {
            BindBankCardDto bindBankCardDto = requestDto.convertToBindBankCardDto();
            String loginName = requestDto.getBaseParam().getUserId();
            AccountModel accountModel = accountMapper.findByLoginName(loginName);
            if(accountModel == null){
                return new BaseResponseDto(ReturnMessage.USER_IS_NOT_CERTIFICATED.getCode(),ReturnMessage.USER_IS_NOT_CERTIFICATED.getMsg());
            }
            BaseDto<PayFormDataDto> requestFormData = bindBankCardService.bindBankCard(bindBankCardDto);
            if(requestFormData.isSuccess()) {
                PayFormDataDto formData = requestFormData.getData();

                BankCardResponseDto dataDto = new BankCardResponseDto();
                dataDto.setUrl(formData.getUrl());
                dataDto.setRequestData(CommonUtils.mapToFormData(formData.getFields(), true));

                baseDto.setCode(ReturnMessage.SUCCESS.getCode());
                baseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
                baseDto.setData(dataDto);
                return baseDto;
            } else {
                log.error("mobile bind card fail, pay wrapper return fail");
            }
        } catch (UnsupportedEncodingException e) {
            log.error("mobile bind card fail", e);
        }
        baseDto.setCode(ReturnMessage.BIND_CARD_FAIL.getCode());
        baseDto.setMessage(ReturnMessage.BIND_CARD_FAIL.getMsg());
        return baseDto;
    }

    @Override
    public BaseResponseDto openFastPay(BankCardRequestDto requestDto) {
        BaseResponseDto baseDto = new BaseResponseDto();
        try {
            AgreementDto agreementDto = requestDto.convertToAgreementDto();
            BaseDto<PayFormDataDto> requestFormData = agreementService.agreement(agreementDto.getLoginName(),agreementDto);
            if(requestFormData.isSuccess()) {
                PayFormDataDto formData = requestFormData.getData();

                BankCardResponseDto dataDto = new BankCardResponseDto();
                dataDto.setUrl(formData.getUrl());
                dataDto.setRequestData(CommonUtils.mapToFormData(formData.getFields(), true));

                baseDto.setCode(ReturnMessage.SUCCESS.getCode());
                baseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
                baseDto.setData(dataDto);
                return baseDto;
            } else {
                log.error("mobile bind card fail, pay wrapper return fail");
            }
        } catch (UnsupportedEncodingException e) {
            log.error("mobile bind card fail", e);
        }
        baseDto.setCode(ReturnMessage.BANK_CARD_SIGN_FAIL.getCode());
        baseDto.setMessage(ReturnMessage.BANK_CARD_SIGN_FAIL.getMsg());
        return baseDto;
    }

    @Override
    public BaseResponseDto queryStatus(BankCardRequestDto requestDto) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        String userId = requestDto.getUserId();
        String operationType = requestDto.getOperationType();
        if (Strings.isNullOrEmpty(operationType) || Strings.isNullOrEmpty(userId)){
            baseResponseDto.setCode(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode());
            baseResponseDto.setCode(ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
            return baseResponseDto;
        }
        BankCardModel bankCardModel = bankCardMapper.findPassedBankCardByLoginName(userId);
        if (MobileAppCommonConstants.QUERY_BIND_STATUS.equals(operationType)){
            //查询绑定状态
            if (bankCardModel != null){
                baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
                baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
            }else {
                baseResponseDto.setCode(ReturnMessage.BIND_CARD_FAIL.getCode());
                baseResponseDto.setMessage(ReturnMessage.BIND_CARD_FAIL.getMsg());
            }
        }else if (MobileAppCommonConstants.QUERY_SIGN_STATUS.equals(operationType)){
            //查询签约状态
            if (bankCardModel != null && bankCardModel.isFastPayOn()){
                baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
                baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
            }else {
                baseResponseDto.setCode(ReturnMessage.BANK_CARD_SIGN_FAIL.getCode());
                baseResponseDto.setMessage(ReturnMessage.BANK_CARD_SIGN_FAIL.getMsg());
            }
        }
        return baseResponseDto;
    }

    @Override
    public BaseResponseDto replaceBankCard(BankCardReplaceRequestDto requestDto) {
        BindBankCardDto bindBankCardDto = requestDto.convertToBindBankCardDto();
        BaseResponseDto<BankCardReplaceResponseDataDto> dto = new BaseResponseDto<>();
        String newCardNo = bindBankCardDto.getCardNumber();
        if (StringUtils.isBlank(newCardNo)) {
            return new BaseResponseDto(ReturnMessage.REPLACE_CARD_FAIL_BANK_CARDNO_IS_NULL.getCode(),ReturnMessage.REPLACE_CARD_FAIL_BANK_CARDNO_IS_NULL.getMsg());
        }
        String loginName = bindBankCardDto.getLoginName();
        UserModel loginUser = userMapper.findByLoginName(loginName);
        if(loginUser == null){
            return new BaseResponseDto(ReturnMessage.USER_ID_NOT_EXIST.getCode(),ReturnMessage.USER_ID_NOT_EXIST.getMsg());
        }
        BankCardModel bankCardModel = bankCardMapper.findByLoginNameAndIsFastPayOn(loginName);
        if(bankCardModel != null){
            return new BaseResponseDto(ReturnMessage.REPLACE_CARD_FAIL_HAS_OPEN_FAST_PAYMENT.getCode(),ReturnMessage.REPLACE_CARD_FAIL_HAS_OPEN_FAST_PAYMENT.getMsg());
        }

        AccountModel accountModel = accountMapper.findByLoginName(loginName);

        BankCardModel bankCardModelIsExist = bankCardMapper.findPassedBankCardByBankCode(newCardNo);
        if(bankCardModelIsExist != null){
            return new BaseResponseDto(ReturnMessage.REPLACE_CARD_FAIL_BANK_CARD_EXIST.getCode(),ReturnMessage.REPLACE_CARD_FAIL_BANK_CARD_EXIST.getMsg());
        }
        BaseDto<PayFormDataDto> requestFormData = bindBankCardService.replaceBankCard(bindBankCardDto);
        if(requestFormData.isSuccess()){
            PayFormDataDto formData = requestFormData.getData();
            BankCardReplaceResponseDataDto bankCardReplaceResponseDataDto = new BankCardReplaceResponseDataDto();
            bankCardReplaceResponseDataDto.setUrl(formData.getUrl());
            try {
                bankCardReplaceResponseDataDto.setRequestData(CommonUtils.mapToFormData(formData.getFields(), true));
            } catch (UnsupportedEncodingException e) {
                return new BaseResponseDto(ReturnMessage.UMPAY_INVEST_MESSAGE_INVALID.getCode(),ReturnMessage.UMPAY_INVEST_MESSAGE_INVALID.getMsg());
            }

            dto.setData(bankCardReplaceResponseDataDto);
        }else{
            log.debug("mobile replace card fail, pay wrapper return fail");
        }
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return dto;
    }
}
