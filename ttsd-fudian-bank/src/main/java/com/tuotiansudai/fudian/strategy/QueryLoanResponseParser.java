package com.tuotiansudai.fudian.strategy;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tuotiansudai.fudian.dto.response.QueryLoanContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

public class QueryLoanResponseParser implements ResponseParserInterface<QueryLoanContentDto> {

    private static Logger logger = LoggerFactory.getLogger(QueryLoanResponseParser.class);

    @Override
    public ResponseDto<QueryLoanContentDto> parse(String data) {
        try {
            ResponseDto<QueryLoanContentDto> dto = gson.fromJson(data, new TypeToken<ResponseDto<QueryLoanContentDto>>() {
            }.getType());
            dto.setReqData(data);
            return dto;
        } catch (JsonSyntaxException e) {
            logger.error(MessageFormat.format("[Response Parse] deserialize error, data is {0}", data), e);
        }

        return null;
    }
}
