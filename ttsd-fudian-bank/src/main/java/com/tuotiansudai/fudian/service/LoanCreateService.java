package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.BankLoanCreateDto;
import com.tuotiansudai.fudian.dto.request.LoanCreateRequestDto;
import com.tuotiansudai.fudian.dto.response.LoanCreateContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
import com.tuotiansudai.fudian.message.BankLoanCreateMessage;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.AmountUtils;
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

    @SuppressWarnings(value = "unchecked")
    public BankLoanCreateMessage create(BankLoanCreateDto bankLoanCreateDto) {
        LoanCreateRequestDto dto = new LoanCreateRequestDto(bankLoanCreateDto.getLoginName(),
                bankLoanCreateDto.getMobile(),
                bankLoanCreateDto.getBankUserName(),
                bankLoanCreateDto.getBankAccountNo(),
                bankLoanCreateDto.getLoanName(),
                AmountUtils.toYuan(bankLoanCreateDto.getAmount()),
                null);

        signatureHelper.sign(dto);

        insertMapper.insertLoanCreate(dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[loan create] sign error, data: {}", bankLoanCreateDto);
            return new BankLoanCreateMessage(false, "签名失败");
        }

        String responseData = bankClient.send(dto.getRequestData(), ApiType.LOAN_CREATE);

        if (!signatureHelper.verifySign(responseData)) {
            logger.error("[loan create] verify sign error, data: {}", bankLoanCreateDto);
            return new BankLoanCreateMessage(false, "验签失败");
        }

        ResponseDto<LoanCreateContentDto> responseDto = (ResponseDto<LoanCreateContentDto>) ApiType.LOAN_CREATE.getParser().parse(responseData);
        if (responseDto == null) {
            logger.error("[loan create] parse response error, data: {}", bankLoanCreateDto);
            return new BankLoanCreateMessage(false, "银行数据解析失败");
        }

        this.updateMapper.updateLoanCreate(responseDto);

        if (responseDto.isSuccess()) {
            LoanCreateContentDto content = responseDto.getContent();
            return new BankLoanCreateMessage(content.getLoanName(), content.getLoanTxNo(), content.getLoanAccNo(), content.getOrderNo(), content.getOrderDate());
        }

        return new BankLoanCreateMessage(false, responseDto.getRetMsg());
    }
}
