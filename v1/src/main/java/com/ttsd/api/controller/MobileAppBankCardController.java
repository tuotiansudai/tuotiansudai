package com.ttsd.api.controller;

import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.util.IdGenerator;
import com.esoft.jdp2p.bankcard.model.BankCard;
import com.esoft.umpay.bankcard.service.impl.UmPayBindingAgreementOperation;
import com.esoft.umpay.bankcard.service.impl.UmPayBindingBankCardOperation;
import com.google.common.base.Strings;
import com.ttsd.api.dto.*;
import com.ttsd.api.service.MobileAppBankCardService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;

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
    private MobileAppBankCardService mobileAppBankCardService;//查询签约／绑卡结果

    @Logger
    private Log log;

    /**
     * @function 绑卡
     * @param bankCardRequestDto 绑卡请求参数
     * @return BaseResponseDto
     */
    @RequestMapping(value = "/bankcard/bind",method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto bankCardBind(@RequestBody BankCardRequestDto bankCardRequestDto){
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        try {
            BankCard bankCard = new BankCard();
            bankCard.setId(IdGenerator.randomUUID());
            bankCard.setCardNo(bankCardRequestDto.getCardNo());
            User user = new User();
            user.setId(bankCardRequestDto.getUserId());
            user.setRealname(bankCardRequestDto.getRealName());
            user.setIdCard(bankCardRequestDto.getIdCard());
            bankCard.setUser(user);
            bankCard.setStatus("uncheck");
            bankCard.setTime(new Date());
            bankCard.setIsOpenFastPayment(bankCardRequestDto.isOpenFastPayment());
            mobileAppBankCardService.save(bankCard);
            BaseResponseDto operation = umPayBindingBankCardOperation.createOperation(bankCard);
            return operation;
        } catch (IOException e) {
            baseResponseDto.setCode(ReturnMessage.NETWORK_EXCEPTION.getCode());
            baseResponseDto.setMessage(ReturnMessage.NETWORK_EXCEPTION.getMsg());
            return baseResponseDto;
        }
    }

    /**
     * @function 换卡
     * @param requestDto 换卡请求参数
     * @return BaseResponseDto
     */
    @RequestMapping(value = "/bankcard/replace",method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto bankCardReplace(@RequestBody BankCardReplaceRequestDto requestDto){
        return mobileAppBankCardService.generateBankCardResponse(requestDto);
    }

    /**
     * @function 签约
     * @param bankCardRequestDto
     * @return BaseResponseDto
     */
    @RequestMapping(value = "/bankcard/sign",method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto bankCardSign(@RequestBody BankCardRequestDto bankCardRequestDto){
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        String userId = bankCardRequestDto.getUserId();
        if (Strings.isNullOrEmpty(userId)){
            baseResponseDto.setCode(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode());
            baseResponseDto.setMessage(ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
            return baseResponseDto;
        }
        try {
            return umPayBindingAgreementOperation.createOperation(userId);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(),e);
            baseResponseDto.setCode(ReturnMessage.NETWORK_EXCEPTION.getCode());
            baseResponseDto.setMessage(ReturnMessage.NETWORK_EXCEPTION.getMsg());
            return baseResponseDto;
        }
    }

    @RequestMapping(value = "/bankcard/query")
    @ResponseBody
    public BaseResponseDto queryBindAndSginStatus(@RequestBody BankCardRequestDto bankCardRequestDto){
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        String userId = bankCardRequestDto.getUserId();
        String operationType = bankCardRequestDto.getOperationType();
        if (Strings.isNullOrEmpty(operationType) || Strings.isNullOrEmpty(userId)){
            baseResponseDto.setCode(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode());
            baseResponseDto.setCode(ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
            return baseResponseDto;
        }
        boolean queryResult = mobileAppBankCardService.queryBindAndSginStatus(bankCardRequestDto.getUserId(),
                bankCardRequestDto.getOperationType());
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
