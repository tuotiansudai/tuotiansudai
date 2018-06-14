package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.BankCardRequestDto;
import com.tuotiansudai.api.dto.v1_0.BankCardResponseDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.service.v1_0.MobileAppBankCardService;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.AgreementService;
import com.tuotiansudai.service.BankBindCardService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileAppBankCardServiceImpl implements MobileAppBankCardService {

    static Logger log = Logger.getLogger(MobileAppBankCardServiceImpl.class);

    @Autowired
    private BankBindCardService bankBindCardService;

    @Autowired
    private AgreementService agreementService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BankAccountMapper bankAccountMapper;

    @Override
    public BaseResponseDto<BankCardResponseDto> bindBankCard(BankCardRequestDto requestDto) {
        BaseResponseDto baseDto = new BaseResponseDto();
//        try {
//            BindBankCardDto bindBankCardDto = requestDto.convertToBindBankCardDto();
//            String loginName = requestDto.getBaseParam().getUserId();
//            BankAccountModel bankAccountModel = bankAccountMapper.findByLoginName(loginName);
//            if (bankAccountModel == null) {
//                return new BaseResponseDto(ReturnMessage.USER_IS_NOT_CERTIFICATED.getCode(), ReturnMessage.USER_IS_NOT_CERTIFICATED.getMsg());
//            }
//            BaseDto<PayFormDataDto> requestFormData = bindBankCardService.bindBankCard(bindBankCardDto);
//            if (requestFormData.isSuccess()) {
//                PayFormDataDto formData = requestFormData.getData();
//
//                BankCardResponseDto dataDto = new BankCardResponseDto();
//                dataDto.setUrl(formData.getUrl());
//                dataDto.setRequestData(CommonUtils.mapToFormData(formData.getFields()));
//
//                baseDto.setCode(ReturnMessage.SUCCESS.getCode());
//                baseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
//                baseDto.setData(dataDto);
//                return baseDto;
//            } else {
//                log.error("mobile bind card fail, pay wrapper return fail");
//            }
//        } catch (UnsupportedEncodingException e) {
//            log.error("mobile bind card fail", e);
//        }
//        baseDto.setCode(ReturnMessage.BIND_CARD_FAIL.getCode());
//        baseDto.setMessage(ReturnMessage.BIND_CARD_FAIL.getMsg());
//        return baseDto;
        return null;
    }
}
