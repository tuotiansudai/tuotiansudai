package com.ttsd.api.service.impl;

import com.esoft.archer.user.exception.UserNotFoundException;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.UserService;
import com.esoft.core.annotations.Logger;
import com.esoft.core.util.IdGenerator;
import com.esoft.jdp2p.bankcard.model.BankCard;
import com.esoft.jdp2p.bankcard.service.BankCardService;
import com.esoft.jdp2p.statistics.controller.BillStatistics;
import com.esoft.umpay.bankcard.service.impl.UmPayReplaceBankCardOperation;
import com.esoft.umpay.trusteeship.exception.UmPayOperationException;
import com.ttsd.api.dao.MobileAppBankCardDao;
import com.ttsd.api.dto.BankCardReplaceRequestDto;
import com.ttsd.api.dto.BankCardReplaceResponseDataDto;
import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.ReturnMessage;
import com.ttsd.api.service.MobileAppBankCardService;
import com.ttsd.api.util.CommonUtils;
import com.umpay.api.common.ReqData;
import com.umpay.api.exception.ReqDataException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.Date;


@Service(value = "mobileAppBankCardServiceImpl")
public class MobileAppBankCardServiceImpl implements MobileAppBankCardService {
    @Logger
    private Log log;

    @Resource(name = "mobileAppBankCardDaoImpl")
    private MobileAppBankCardDao mobileAppBankCardDao;

    @Autowired
    private UserService userService;

    @Autowired
    private BankCardService bankCardService;

    @Autowired
    private UmPayReplaceBankCardOperation umPayReplaceBankCardOperation;

    @Autowired
    private BillStatistics billStatistics;

    @Autowired
    HibernateTemplate hibernateTemplate;
    /**
     * @param userId 绑卡或签约用户ID
     * @return boolean
     * @function 查询绑卡状态
     */
    @Override
    public boolean queryBindAndSginStatus(String userId, String operationType) {
        int count = mobileAppBankCardDao.queryBindAndSginStatus(userId, operationType);
        return count > 0;
    }

    @Override
    public BaseResponseDto generateBankCardResponse(BankCardReplaceRequestDto requestDto) {
        String newCardNo = requestDto.getCardNo();
        String userId = requestDto.getBaseParam().getUserId();

        BaseResponseDto<BankCardReplaceResponseDataDto> dto = new BaseResponseDto<>();

        // verify cardNo
        if (StringUtils.isBlank(newCardNo)) {
            dto.setCode(ReturnMessage.REPLACE_CARD_FAIL_BANK_CARDNO_IS_NULL.getCode());
            dto.setMessage(ReturnMessage.REPLACE_CARD_FAIL_BANK_CARDNO_IS_NULL.getMsg());
            return dto;
        }

        // verify user
        User loginUser = null;
        try {
            loginUser = userService.getUserById(userId);
        } catch (UserNotFoundException e) {
            dto.setCode(ReturnMessage.USER_ID_NOT_EXIST.getCode());
            dto.setMessage(ReturnMessage.USER_ID_NOT_EXIST.getMsg());
            return dto;
        }

        // verify fast payment
        if (bankCardService.isOpenFastPayment(userId)) {
            dto.setCode(ReturnMessage.REPLACE_CARD_FAIL_HAS_OPEN_FAST_PAYMENT.getCode());
            dto.setMessage(ReturnMessage.REPLACE_CARD_FAIL_HAS_OPEN_FAST_PAYMENT.getMsg());
            return dto;
        }

        // verify account is 0 or not
        if (billStatistics.getBalanceByUserId(userId) > 0.0) {
            dto.setCode(ReturnMessage.REPLACE_CARD_FAIL_ACCOUNT_BALANCE_IS_NOT_ZERO.getCode());
            dto.setMessage(ReturnMessage.REPLACE_CARD_FAIL_ACCOUNT_BALANCE_IS_NOT_ZERO.getMsg());
            return dto;
        }

        // verify card exists
        if (bankCardService.isCardNoBinding(newCardNo)) {
            dto.setCode(ReturnMessage.REPLACE_CARD_FAIL_BANK_CARD_EXIST.getCode());
            dto.setMessage(ReturnMessage.REPLACE_CARD_FAIL_BANK_CARD_EXIST.getMsg());
            return dto;
        }

        try {
            BankCardReplaceResponseDataDto responseDataDto = getBankCardReplaceResponseDataDto(newCardNo, loginUser);
            dto.setCode(ReturnMessage.SUCCESS.getCode());
            dto.setMessage(ReturnMessage.SUCCESS.getMsg());
            dto.setData(responseDataDto);
        } catch (UmPayOperationException e) {
            dto.setCode(ReturnMessage.UMPAY_OPERATION_EXCEPTION.getCode());
            dto.setMessage(ReturnMessage.UMPAY_OPERATION_EXCEPTION.getMsg() + ":" + e.getLocalizedMessage());
            log.warn(ReturnMessage.UMPAY_OPERATION_EXCEPTION.getMsg(), e);
        } catch (ReqDataException e) {
            dto.setCode(ReturnMessage.UMPAY_INVEST_MESSAGE_INVALID.getCode());
            dto.setMessage(ReturnMessage.UMPAY_INVEST_MESSAGE_INVALID.getMsg());
            log.warn(ReturnMessage.UMPAY_INVEST_MESSAGE_INVALID.getMsg(), e);
        }
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(BankCard bankCard) {
        hibernateTemplate.save(bankCard);
    }

    private BankCardReplaceResponseDataDto getBankCardReplaceResponseDataDto(String newCardNo, User user) throws ReqDataException{
        BankCard bankCard = new BankCard();
        bankCard.setCardNo(newCardNo);
        bankCard.setId(IdGenerator.randomUUID());
        bankCard.setUser(user);
        bankCard.setStatus("uncheck");
        bankCard.setTime(new Date());
        bankCard.setIsOpenFastPayment(false);

        ReqData reqData = umPayReplaceBankCardOperation.createOperation_mobile(bankCard);
        BankCardReplaceResponseDataDto responseDataDto = new BankCardReplaceResponseDataDto();
        try {
            responseDataDto.setRequestData(CommonUtils.mapToFormData(reqData.getField(), true));
        } catch (UnsupportedEncodingException e) {
            // 因为CommonUtils.mapToFormData采用的编码格式为UTF-8，因此不会不支持，此异常不会出现
            log.warn("Encoding失败", e);
        }
        responseDataDto.setUrl(reqData.getUrl());
        return responseDataDto;
    }

}
