package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.request.QueryTradeRequestDto;
import com.tuotiansudai.fudian.dto.request.QueryTradeType;
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

    private final SignatureHelper signatureHelper;

    private final BankClient bankClient;

    @Autowired
    public QueryTradeService(SignatureHelper signatureHelper, BankClient bankClient) {
        this.signatureHelper = signatureHelper;
        this.bankClient = bankClient;
    }

    public ResponseDto query(String orderNo, String orderDate, QueryTradeType queryType) {
        QueryTradeRequestDto dto = new QueryTradeRequestDto(orderNo, orderDate, queryType.getValue());

        signatureHelper.sign(dto);
        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[query trade] sign error, orderNo: {}, orderDate: {}, queryType: {}", orderNo, orderDate, queryType);
            return null;
        }

        String responseData = bankClient.send(dto.getRequestData(), ApiType.QUERY_TRADE);
        if (Strings.isNullOrEmpty(responseData)) {
            logger.error("[query trade] send error, orderNo: {}, orderDate: {}, queryType: {}", orderNo, orderDate, queryType);
            return null;
        }

        if (!signatureHelper.verifySign(responseData)) {
            logger.error("[query trade] verify sign error, orderNo: {}, orderDate: {}, queryType: {}", orderNo, orderDate, queryType);
            return null;
        }

        ResponseDto responseDto = ApiType.QUERY_TRADE.getParser().parse(responseData);
        if (responseDto == null) {
            logger.error("[query trade] parse response error, orderNo: {}, orderDate: {}, queryType: {}", orderNo, orderDate, queryType);
            return null;
        }

        return responseDto;
    }
}
