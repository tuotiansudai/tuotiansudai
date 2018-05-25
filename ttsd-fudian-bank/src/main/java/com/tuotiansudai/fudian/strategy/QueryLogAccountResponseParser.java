package com.tuotiansudai.fudian.strategy;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tuotiansudai.fudian.dto.response.QueryLogAccountContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

public class QueryLogAccountResponseParser implements ResponseParserInterface<QueryLogAccountContentDto> {

    private static Logger logger = LoggerFactory.getLogger(QueryLogAccountResponseParser.class);

    @Override
    public ResponseDto<QueryLogAccountContentDto> parse(String data) {
        try {
            ResponseDto<QueryLogAccountContentDto> dto = gson.fromJson(data, new TypeToken<ResponseDto<QueryLogAccountContentDto>>() {
            }.getType());
            dto.setReqData(data);
            return dto;
        } catch (JsonSyntaxException e) {
            logger.error(MessageFormat.format("[Response Parse] deserialize error, data is {0}", data), e);
        }

        return null;
    }
}
