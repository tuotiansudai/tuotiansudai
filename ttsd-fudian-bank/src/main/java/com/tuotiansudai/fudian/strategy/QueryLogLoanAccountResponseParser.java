package com.tuotiansudai.fudian.strategy;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tuotiansudai.fudian.dto.response.QueryLogLoanAccountContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

public class QueryLogLoanAccountResponseParser implements ResponseParserInterface<QueryLogLoanAccountContentDto> {

    private static Logger logger = LoggerFactory.getLogger(QueryLogLoanAccountResponseParser.class);

    @Override
    public ResponseDto<QueryLogLoanAccountContentDto> parse(String data) {
        try {
            ResponseDto<QueryLogLoanAccountContentDto> dto = gson.fromJson(data, new TypeToken<ResponseDto<QueryLogLoanAccountContentDto>>() {
            }.getType());
            dto.setReqData(data);
            return dto;
        } catch (JsonSyntaxException e) {
            logger.error(MessageFormat.format("[Response Parse] deserialize error, data is {0}", data), e);
        }

        return null;
    }
}
