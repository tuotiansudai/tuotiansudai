package com.tuotiansudai.fudian.strategy;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tuotiansudai.fudian.dto.response.QueryUserContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

public class QueryUserResponseParser implements ResponseParserInterface<QueryUserContentDto> {

    private static Logger logger = LoggerFactory.getLogger(QueryUserResponseParser.class);

    @Override
    public ResponseDto<QueryUserContentDto> parse(String data) {
        try {
            ResponseDto<QueryUserContentDto> dto = gson.fromJson(data, new TypeToken<ResponseDto<QueryUserContentDto>>() {
            }.getType());
            dto.setReqData(data);
            return dto;
        } catch (JsonSyntaxException e) {
            logger.error(MessageFormat.format("[Response Parse] deserialize error, data is {0}", data), e);
        }

        return null;
    }
}
