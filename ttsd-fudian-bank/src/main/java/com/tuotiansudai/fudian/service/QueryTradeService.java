package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.request.QueryTradeRequestDto;
import com.tuotiansudai.fudian.dto.request.QueryTradeType;
import com.tuotiansudai.fudian.dto.response.QueryTradeContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.BankClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueryTradeService {

    private static Logger logger = LoggerFactory.getLogger(QueryTradeService.class);

    private static final ApiType API_TYPE = ApiType.QUERY_TRADE;

    private final SignatureHelper signatureHelper;

    private final BankClient bankClient;

    @Autowired
    public QueryTradeService(SignatureHelper signatureHelper, BankClient bankClient) {
        this.signatureHelper = signatureHelper;
        this.bankClient = bankClient;
    }

    @SuppressWarnings(value = "unchecked")
    public ResponseDto<QueryTradeContentDto> query(String orderNo, String orderDate, QueryTradeType queryType) {
        QueryTradeRequestDto dto = new QueryTradeRequestDto(orderNo, orderDate, queryType.getValue());

        signatureHelper.sign(API_TYPE, dto);
        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            return null;
        }

        String responseData = bankClient.send(API_TYPE, dto.getRequestData());

        if (!signatureHelper.verifySign(responseData)) {
            logger.warn("[query trade] failed to verify sign, orderNo: {}, orderDate: {}, queryType: {}, response: {}", orderNo, orderDate, queryType, responseData);
            return null;
        }

        return (ResponseDto<QueryTradeContentDto>) API_TYPE.getParser().parse(responseData);
    }
}
