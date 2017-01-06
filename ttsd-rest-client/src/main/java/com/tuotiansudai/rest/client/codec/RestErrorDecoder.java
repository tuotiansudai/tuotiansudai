package com.tuotiansudai.rest.client.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.rest.client.dto.ErrorResponse;
import com.tuotiansudai.rest.client.exceptions.RestException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class RestErrorDecoder implements ErrorDecoder {
    private static Logger logger = Logger.getLogger(RestErrorDecoder.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Exception decode(String methodKey, Response response) {
        ErrorResponse errorResponse = null;
        try {
            errorResponse = objectMapper.readValue(response.body().asInputStream(), ErrorResponse.class);
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error("can not parse response body as ErrorResponse: " + response.body().toString());
            }
        }
        return new RestException(response, errorResponse);
    }
}
