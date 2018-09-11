package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.request.QueryLogAccountRequestDto;
import com.tuotiansudai.fudian.dto.response.QueryLogAccountContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.BankClient;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class QueryLogAccountService {

    private static Logger logger = LoggerFactory.getLogger(QueryLogAccountService.class);

    private static final ApiType API_TYPE = ApiType.QUERY_LOG_ACCOUNT;

    private final SignatureHelper signatureHelper;

    private final BankClient bankClient;

    @Autowired
    public QueryLogAccountService(SignatureHelper signatureHelper, BankClient bankClient) {
        this.signatureHelper = signatureHelper;
        this.bankClient = bankClient;
    }

    @SuppressWarnings(value = "unchecked")
    public List<ResponseDto<QueryLogAccountContentDto>> query(String userName, String accountNo, Date queryOrderDateStart, Date queryOrderDateEnd) {
        DateTime startDate = new DateTime(queryOrderDateStart);
        DateTime endDate = new DateTime(queryOrderDateEnd);

        List<ResponseDto<QueryLogAccountContentDto>> results = Lists.newArrayList();
        while (!startDate.isAfter(endDate)) {
            QueryLogAccountRequestDto dto = new QueryLogAccountRequestDto(userName, accountNo, startDate.toString("yyyyMMdd"));

            signatureHelper.sign(API_TYPE, dto);
            if (Strings.isNullOrEmpty(dto.getRequestData())) {
                return null;
            }

            String responseData = bankClient.send(API_TYPE, dto.getRequestData());

            if (!signatureHelper.verifySign(responseData)) {
                logger.warn("[query log account] failed to verify sign, userName: {}, accountNo: {}, queryOrderDate: {}, response: {}", userName, accountNo, startDate.toString("yyyyMMdd"), responseData);
                return null;
            }

            ResponseDto<QueryLogAccountContentDto> responseDto = API_TYPE.getParser().parse(responseData);

            if (responseDto == null) {
                return null;
            }

            if (responseDto.isSuccess()) {
                results.add(responseDto);
            }

            startDate = startDate.plusDays(1);
        }
        Collections.reverse(results);
        return results;
    }
}
