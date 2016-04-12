package com.ttsd.api.service.impl;

import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.jdp2p.bankcard.model.BankCard;
import com.esoft.jdp2p.bankcard.service.BankCardService;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;
import com.esoft.jdp2p.loan.model.WithdrawCash;
import com.esoft.jdp2p.user.service.WithdrawCashService;
import com.esoft.umpay.withdraw.service.impl.UmPayWithdrawOperation;
import com.ttsd.api.dto.*;
import com.ttsd.api.service.MobileAppWithdrawService;
import com.ttsd.api.util.CommonUtils;
import com.umpay.api.common.ReqData;
import com.umpay.api.exception.ReqDataException;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class MobileAppWithdrawServiceImpl implements MobileAppWithdrawService {
    @Autowired
    private WithdrawCashService withdrawCashService;

    @Autowired
    private UmPayWithdrawOperation umPayWithdrawOperation;

    @Autowired
    private BankCardService bankCardService;

    @Logger
    static Log log;

    @Override
    public BaseResponseDto queryUserWithdrawLogs(WithdrawListRequestDto requestDto) {
        String userId = requestDto.getBaseParam().getUserId();
        Integer index = requestDto.getIndex();
        Integer pageSize = requestDto.getPageSize();

        Integer withdrawCashCount = withdrawCashService.queryUserWithdrawLogsCount(userId);
        List<WithdrawCash> withdrawCashList = withdrawCashService.queryUserWithdrawLogs(userId, (index - 1) * pageSize, pageSize);

        List<WithdrawDetailResponseDataDto> withdrawResponseList = new ArrayList<>();
        if (withdrawCashList != null) {
            for (WithdrawCash withdrawCash : withdrawCashList) {
                withdrawResponseList.add(new WithdrawDetailResponseDataDto(withdrawCash));
            }
        }

        WithdrawListResponseDataDto listDataDto = new WithdrawListResponseDataDto();
        listDataDto.setIndex(index);
        listDataDto.setPageSize(pageSize);
        listDataDto.setTotalCount(withdrawCashCount);
        listDataDto.setWithdrawList(withdrawResponseList);

        BaseResponseDto<WithdrawListResponseDataDto> dto = new BaseResponseDto<>();
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(listDataDto);

        return dto;
    }

    @Override
    public BaseResponseDto generateWithdrawRequest(WithdrawOperateRequestDto requestDto) {
        String userId = requestDto.getBaseParam().getUserId();
        double money = requestDto.getMoney();
        String platform = requestDto.getBaseParam().getPlatform();
        BaseResponseDto<WithdrawOperateResponseDataDto> dto = new BaseResponseDto<>();

        // verify bank card
        List<BankCard> bankCards = bankCardService.getBoundBankCardsByUserId(userId);
        if (bankCards == null || bankCards.size() == 0) {
            dto.setCode(ReturnMessage.NOT_BIND_CARD.getCode());
            dto.setMessage(ReturnMessage.NOT_BIND_CARD.getMsg());
        }else {
            try {
                WithdrawOperateResponseDataDto responseDataDto = getWithdrawOperateResponseDataDto(userId, money, bankCards.get(0),AccessSource.valueOf(platform.toUpperCase(Locale.ENGLISH)));
                // success
                dto.setCode(ReturnMessage.SUCCESS.getCode());
                dto.setMessage(ReturnMessage.SUCCESS.getMsg());
                dto.setData(responseDataDto);
            } catch (InsufficientBalance insufficientBalance) {
                dto.setCode(ReturnMessage.NOT_SUFFICIENT_FUNDS.getCode());
                dto.setMessage(ReturnMessage.NOT_SUFFICIENT_FUNDS.getMsg());
                log.warn("提现失败：余额不足", insufficientBalance);
            } catch (ReqDataException e) {
                dto.setCode(ReturnMessage.UMPAY_INVEST_MESSAGE_INVALID.getCode());
                dto.setMessage(ReturnMessage.UMPAY_INVEST_MESSAGE_INVALID.getMsg());
                log.warn("提现失败：第三方加密失败", e);
            }
        }
        return dto;
    }

    private WithdrawOperateResponseDataDto getWithdrawOperateResponseDataDto(String userId, double money, BankCard bankCard, AccessSource accessSource) throws InsufficientBalance, ReqDataException {
        // create Withdraw apply
        WithdrawCash withdrawCash = new WithdrawCash();
        withdrawCash.setUser(new User(userId));
        withdrawCash.setAccount("借款账户");
        withdrawCash.setFee(withdrawCashService.calculateFee(money));
        withdrawCash.setCashFine(0D);
        withdrawCash.setMoney(money);
        withdrawCash.setBankCard(bankCard);
        withdrawCash.setSource(accessSource.name());
        withdrawCashService.applyWithdrawCash(withdrawCash);

        ReqData reqData = umPayWithdrawOperation.createOperation_mobile(withdrawCash);

        // build api response data
        WithdrawOperateResponseDataDto responseDataDto = new WithdrawOperateResponseDataDto();
        responseDataDto.setUrl(reqData.getUrl());
        try {
            responseDataDto.setRequestData(CommonUtils.mapToFormData(reqData.getField(),true));
        } catch (UnsupportedEncodingException e) {
            // 因为CommonUtils.mapToFormData采用的编码格式为UTF-8，因此不会不支持，此异常不会出现
        }
        return responseDataDto;
    }
}
