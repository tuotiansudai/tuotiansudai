package com.tuotiansudai.fudian.strategy;

import com.google.common.base.Strings;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tuotiansudai.fudian.dto.response.QueryTradeContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

public class QueryTradeResponseParser implements ResponseParserInterface<QueryTradeContentDto> {

    private static Logger logger = LoggerFactory.getLogger(QueryTradeResponseParser.class);

    @Override
    public ResponseDto<QueryTradeContentDto> parse(String data) {
        if (Strings.isNullOrEmpty(data)) {
            return null;
        }
        try {
            ResponseDto<QueryTradeContentDto> dto = gson.fromJson(data, new TypeToken<ResponseDto<QueryTradeContentDto>>() {
            }.getType());
            dto.setReqData(data);
            return dto;
        } catch (JsonSyntaxException e) {
            logger.error(MessageFormat.format("[Response Parse] failed to deserialize, data is {0}", data), e);
        }

        return null;
    }
}
