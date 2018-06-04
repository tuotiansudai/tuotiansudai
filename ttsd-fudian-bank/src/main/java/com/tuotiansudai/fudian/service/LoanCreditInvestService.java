package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.request.LoanCreditInvestRequestDto;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.dto.response.LoanCreateContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.SelectMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanCreditInvestService implements ReturnCallbackInterface, NotifyCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(LoanCreditInvestService.class);

    private static final ApiType API_TYPE = ApiType.LOAN_CREDIT_INVEST;

    private final SignatureHelper signatureHelper;

    private final SelectMapper selectMapper;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    @Autowired
    public LoanCreditInvestService(SignatureHelper signatureHelper, SelectMapper selectMapper, InsertMapper insertMapper, UpdateMapper updateMapper) {
        this.signatureHelper = signatureHelper;
        this.selectMapper = selectMapper;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
    }

    public LoanCreditInvestRequestDto invest(Source source, String loginName, String mobile, String userName, String accountNo, String loanTxNo, String investOrderNo, String investOrderDate, String creditNo, String creditAmount, String amount, String creditFee) {
        LoanCreditInvestRequestDto dto = new LoanCreditInvestRequestDto(source, loginName, mobile, userName, accountNo, loanTxNo, investOrderNo, investOrderDate, creditNo, creditAmount, amount, creditFee, null);
        signatureHelper.sign(API_TYPE, dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[loan credit invest] sign error, userName: {}, accountNo: {}, loanTxNo: {}, investOrderNo: {}, investOrderDate: {}, creditNo: {}, creditAmount: {}, amount: {}, creditFee: {}",
                    userName, accountNo, loanTxNo, investOrderNo, investOrderDate, creditNo, creditAmount, amount, creditFee);
            return null;
        }

        insertMapper.insertLoanCreditInvest(dto);
        return dto;
    }

    @Override
    public void returnCallback(ResponseDto responseData) {
        updateMapper.updateReturnResponse(API_TYPE.name().toLowerCase(), responseData);
    }

    @Override
    public ResponseDto notifyCallback(String responseData) {
        logger.info("[loan credit invest] data is {}", responseData);
        ResponseDto responseDto = API_TYPE.getParser().parse(responseData);

        if (responseDto == null) {
            logger.error("[loan credit invest] parse callback data error, data is {}", responseData);
            return null;
        }
        responseDto.setReqData(responseData);
        updateMapper.updateNotifyResponseData(API_TYPE.name().toLowerCase(), responseDto);
        return responseDto;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean isSuccess(String orderNo) {
        String responseData = this.selectMapper.selectNotifyResponseData(API_TYPE.name().toLowerCase(), orderNo);
        if (Strings.isNullOrEmpty(responseData)) {
            return null;
        }

        ResponseDto<LoanCreateContentDto> responseDto = (ResponseDto<LoanCreateContentDto>) API_TYPE.getParser().parse(responseData);

        return responseDto.isSuccess();
    }
}
