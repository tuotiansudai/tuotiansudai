package com.ttsd.api.controller;

import com.esoft.core.annotations.Logger;
import com.esoft.jdp2p.bankcard.model.BankCard;
import com.esoft.umpay.bankcard.service.impl.UmPayBindingAgreementOperation;
import com.esoft.umpay.bankcard.service.impl.UmPayBindingBankCardOperation;
import com.google.common.base.Strings;
import com.ttsd.api.dto.*;
import com.ttsd.api.service.MobileAppBankCardService;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by tuotian on 15/8/7.
 */
@Controller
public class MobileAppBankCardController {
    @Resource
    private UmPayBindingAgreementOperation umPayBindingAgreementOperation;//签约

    @Resource
    private UmPayBindingBankCardOperation umPayBindingBankCardOperation;//绑卡

    @Resource(name = "mobileAppBankCardServiceImpl")
    private MobileAppBankCardService mobileAppBankCardService;

    @Logger
    private Log log;

    /**
     * @function 绑卡
     * @param bindBankCardRequestDto 绑卡请求参数
     * @return BaseResponseDto
     */
    @RequestMapping(value = "/bankcard/bind",method = RequestMethod.POST)
    public BaseResponseDto bankCardBind(@RequestBody BindBankCardRequestDto bindBankCardRequestDto){
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        try {
            BankCard bankCard = new BankCard();
            return umPayBindingBankCardOperation.createOperation(bankCard);
        } catch (IOException e) {
            baseResponseDto.setCode(ReturnMessage.NETWORK_EXCEPTION.getCode());
            baseResponseDto.setMessage(ReturnMessage.NETWORK_EXCEPTION.getMsg());
        }
        return baseResponseDto;
    }

    /**
     * @function 签约
     * @param signBankCardRequestDto
     * @return BaseResponseDto
     */
    @RequestMapping(value = "/bankcard/sign",method = RequestMethod.POST)
    public BaseResponseDto bankcardSign(@RequestBody SignBankCardRequestDto signBankCardRequestDto){
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        String userId = signBankCardRequestDto.getBaseParam().getUserId();
        if (Strings.isNullOrEmpty(userId)){
            baseResponseDto.setCode(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode());
            baseResponseDto.setMessage(ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
            return baseResponseDto;
        }
        try {
            umPayBindingAgreementOperation.createOperation(userId);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(),e);
        }
        return baseResponseDto;
    }

    @RequestMapping(value = "bankcard/query")
    public BaseResponseDto queryBindAndSginStatus(@RequestBody QueryBindAndSignStatusRequestDto queryBindAndSignStatusRequestDto){
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        String userId = queryBindAndSignStatusRequestDto.getUserId();
        String operationType = queryBindAndSignStatusRequestDto.getOperationType();
        if (Strings.isNullOrEmpty(operationType) || Strings.isNullOrEmpty(userId)){
            baseResponseDto.setCode(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode());
            baseResponseDto.setCode(ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
            return baseResponseDto;
        }
        boolean queryResult = mobileAppBankCardService.queryBindAndSginStatus(queryBindAndSignStatusRequestDto.getUserId(),
                queryBindAndSignStatusRequestDto.getOperationType());
        if (MobileAppCommonConstants.QUERY_BIND_STATUS.equals(operationType)){
            //查询绑定状态
            if (queryResult){
                baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
                baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
            }else {
                baseResponseDto.setCode(ReturnMessage.BIND_CARD_FAIL.getCode());
                baseResponseDto.setMessage(ReturnMessage.BIND_CARD_FAIL.getMsg());
            }

        }else if (MobileAppCommonConstants.QUERY_SIGN_STATUS.equals(operationType)){
            //查询签约状态
            if (queryResult){
                baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
                baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
            }else {
                baseResponseDto.setCode(ReturnMessage.BANK_CARD_SIGN_FAIL.getCode());
                baseResponseDto.setMessage(ReturnMessage.BANK_CARD_SIGN_FAIL.getMsg());
            }
        }
        return baseResponseDto;
    }
}
