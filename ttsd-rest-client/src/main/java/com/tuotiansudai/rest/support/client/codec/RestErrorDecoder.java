package com.tuotiansudai.rest.support.client.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.rest.support.client.dto.ErrorResponse;
import com.tuotiansudai.rest.support.client.exceptions.RestException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.apache.log4j.Logger;

import java.io.IOException;

public class RestErrorDecoder implements ErrorDecoder {
    private static Logger logger = Logger.getLogger(RestErrorDecoder.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        ErrorResponse errorResponse = null;
        if (response.body() != null)
            try {
                errorResponse = objectMapper.readValue(response.body().asInputStream(), ErrorResponse.class);
            } catch (IOException e) {
                logger.warn("can not parse response body as ErrorResponse: " + response.body().toString());
            }

        return new RestException(response, errorResponse);
    }
}
