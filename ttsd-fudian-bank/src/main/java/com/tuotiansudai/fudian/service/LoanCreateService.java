package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.request.LoanCreateRequestDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.BankClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanCreateService {

    private static Logger logger = LoggerFactory.getLogger(LoanCreateService.class);

    private final SignatureHelper signatureHelper;

    private final BankClient bankClient;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    @Autowired
    public LoanCreateService(SignatureHelper signatureHelper, BankClient bankClient, InsertMapper insertMapper, UpdateMapper updateMapper) {
        this.signatureHelper = signatureHelper;
        this.bankClient = bankClient;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
    }

    public ResponseDto create(String userName, String accountNo, String amount, String loanName, String loginName, String mobile) {
        LoanCreateRequestDto dto = new LoanCreateRequestDto(userName, accountNo, amount, loanName, loginName, mobile);
        signatureHelper.sign(dto, ApiType.LOAN_CREATE);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[loan create] sign error, userName: {}, accountNo: {}, amount: {}, loanName: {}",
                    userName, accountNo, amount, loanName);
        }

        insertMapper.insertLoanCreate(dto);

        String responseData = bankClient.send(dto.getRequestData(), ApiType.LOAN_CREATE);

        if (Strings.isNullOrEmpty(responseData)) {
            logger.error("[loan create] sign error, userName: {}, accountNo: {}, amount: {}, loanName: {}",
                    userName, accountNo, amount, loanName);
            return null;
        }

        if (!signatureHelper.verifySign(responseData)) {
            logger.error("[loan create] verify sign error, userName: {}, accountNo: {}, amount: {}, loanName: {}",
                    userName, accountNo, amount, loanName);
            return null;
        }

        ResponseDto responseDto = ApiType.LOAN_CREATE.getParser().parse(responseData);
        if (responseDto == null) {
            logger.error("[loan create] parse response error, userName: {}, accountNo: {}, amount: {}, loanName: {}",
                    userName, accountNo, amount, loanName);
            return null;
        }

        this.updateMapper.updateLoanCreate(responseDto);
        return responseDto;
    }
}
