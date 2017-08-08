package com.tuotiansudai.rest.support.client.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.rest.support.client.dto.ErrorResponse;
import com.tuotiansudai.rest.support.client.exceptions.RestException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.apache.log4j.Logger;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RestErrorDecoder implements ErrorDecoder {
    private static Logger logger = Logger.getLogger(RestErrorDecoder.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        ErrorResponse errorResponse = null;
        String responseBody = null;
        if (response.body() != null) {
            try {
                responseBody = StreamUtils.copyToString(response.body().asInputStream(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                logger.error("can not read response body");
            }
            try {
                errorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);
            } catch (IOException e) {
                logger.error("can not parse response body as ErrorResponse: '" + responseBody + "'");
            }
        }
        return new RestException(response, errorResponse);
    }
}
