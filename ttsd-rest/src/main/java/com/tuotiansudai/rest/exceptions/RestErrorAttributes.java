package com.tuotiansudai.rest.exceptions;

import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.RequestDispatcher;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestErrorAttributes extends DefaultErrorAttributes
        implements ErrorAttributes, HandlerExceptionResolver, Ordered {

    @Override
    public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes,
                                                  boolean includeStackTrace) {
        Map<String, Object> errorAttributes = new LinkedHashMap<>();

        Throwable cause = getError(requestAttributes);
        ErrorCode errorCode = getErrorCode(cause);
        buildErrorAttribute(errorAttributes, errorCode, cause);

        addPathAttribute(errorAttributes, requestAttributes);
        errorAttributes.put("timestamp", new Date());
        return errorAttributes;
    }

    private ErrorCode getErrorCode(Throwable cause) {
        if (cause != null) {
            if (cause instanceof RestException) {
                return ((RestException) cause).getErrorCode();
            }
            if (cause instanceof MethodArgumentNotValidException) {
                return RestErrorCode.RequestDataValidFailed;
            }
            return RestErrorCode.UnexpectedError;
        }
        return RestErrorCode.Empty;
    }

    private void buildErrorAttribute(Map<String, Object> errorAttributes, ErrorCode errorCode, Throwable ex) {
        errorAttributes.put("code", errorCode.getCode());
        errorAttributes.put("message", errorCode.getMessage());
        errorAttributes.put("exception", extraExceptionMessage(ex));
    }

    private void addPathAttribute(Map<String, Object> errorAttributes, RequestAttributes requestAttributes) {
        String path = String.valueOf(requestAttributes.getAttribute(RequestDispatcher.ERROR_REQUEST_URI, RequestAttributes.SCOPE_REQUEST));
        if (path != null) {
            errorAttributes.put("path", path);
        }
    }

    private String extraExceptionMessage(Throwable ex) {
        if (ex == null) {
            return "";
        }
        String message = ex.getLocalizedMessage();
        if (StringUtils.isEmpty(message)) {
            message = ex.getClass().getCanonicalName();
        }
        return message;
    }
}
