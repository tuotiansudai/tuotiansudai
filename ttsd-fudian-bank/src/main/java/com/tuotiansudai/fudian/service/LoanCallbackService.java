package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.google.protobuf.Api;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.request.LoanCallbackInvestItemRequestDto;
import com.tuotiansudai.fudian.dto.request.LoanCallbackRequestDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.BankClient;
import com.tuotiansudai.fudian.util.OrderIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanCallbackService {

    private static Logger logger = LoggerFactory.getLogger(LoanCallbackService.class);

    private final RedisTemplate<String, String> redisTemplate;

    private final BankClient bankClient;

    private final SignatureHelper signatureHelper;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    @Autowired
    public LoanCallbackService(RedisTemplate<String, String> redisTemplate, BankClient bankClient, SignatureHelper signatureHelper, InsertMapper insertMapper, UpdateMapper updateMapper) {
        this.bankClient = bankClient;
        this.signatureHelper = signatureHelper;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
        this.redisTemplate = redisTemplate;
    }

    public ResponseDto loanCallback(String loanTxNo, List<LoanCallbackInvestItemRequestDto> investItems, String loginName, String mobile) {
        LoanCallbackRequestDto dto = new LoanCallbackRequestDto(loanTxNo, investItems, loginName, mobile);

        investItems.forEach(investItem -> {
            investItem.setOrderNo(OrderIdGenerator.generate(redisTemplate));
            investItem.setOrderDate(dto.getOrderDate());
        });

        signatureHelper.sign(dto, ApiType.LOAN_CALLBACK);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[loan callback] sign error, loanTxNo: {}", loanTxNo);
            investItems.forEach(investItem -> logger.warn("[loan callback] sign error, capital: {}, interest: {}, interestFee: {}, rateInterest: {}, investUserName: {}, investAccountNo: {}, investOrderNo: {}, investOrderDate;",
                    investItem.getCapital(), investItem.getInterest(), investItem.getInterestFee(), investItem.getRateInterest(), investItem.getInvestUserName(), investItem.getInvestAccountNo(), investItem.getInvestOrderNo(), investItem.getInvestOrderDate()));
            return null;
        }

        insertMapper.insertLoanCallback(dto);
        investItems.forEach(investItem -> {
            investItem.setLoanCallbackId(dto.getId());
        });
        insertMapper.insertLoanCallbackInvestItems(investItems);

        String responseData = bankClient.send(dto.getRequestData(), ApiType.LOAN_CALLBACK);
        if (Strings.isNullOrEmpty(responseData)) {
            logger.error("[loan callback] send error, loanTxNo: {}", loanTxNo);
            investItems.forEach(investItem -> logger.warn("[loan callback] send error, capital: {}, interest: {}, interestFee: {}, rateInterest: {}, investUserName: {}, investAccountNo: {}, investOrderNo: {}, investOrderDate;",
                    investItem.getCapital(), investItem.getInterest(), investItem.getInterestFee(), investItem.getRateInterest(), investItem.getInvestUserName(), investItem.getInvestAccountNo(), investItem.getInvestOrderNo(), investItem.getInvestOrderDate()));
            return null;
        }

        if (!signatureHelper.verifySign(responseData)) {
            logger.error("[loan callback] verify sign error, loanTxNo: {}", loanTxNo);
            investItems.forEach(investItem -> logger.warn("[loan callback] send error, capital: {}, interest: {}, interestFee: {}, rateInterest: {}, investUserName: {}, investAccountNo: {}, investOrderNo: {}, investOrderDate;",
                    investItem.getCapital(), investItem.getInterest(), investItem.getInterestFee(), investItem.getRateInterest(), investItem.getInvestUserName(), investItem.getInvestAccountNo(), investItem.getInvestOrderNo(), investItem.getInvestOrderDate()));
            return null;
        }

        ResponseDto responseDto = ApiType.LOAN_CALLBACK.getParser().parse(responseData);
        if (responseDto == null) {
            logger.error("[loan callback] parse response error, loanTxNo: {}", loanTxNo);
            investItems.forEach(investItem -> logger.warn("[loan callback] parse response error, capital: {}, interest: {}, interestFee: {}, rateInterest: {}, investUserName: {}, investAccountNo: {}, investOrderNo: {}, investOrderDate;",
                    investItem.getCapital(), investItem.getInterest(), investItem.getInterestFee(), investItem.getRateInterest(), investItem.getInvestUserName(), investItem.getInvestAccountNo(), investItem.getInvestOrderNo(), investItem.getInvestOrderDate()));
            return null;
        }

        this.updateMapper.updateLoanCallback(responseDto);
        return responseDto;
    }
}
