package com.tuotiansudai.rest.handler;

import com.tuotiansudai.rest.exceptions.RestException;
import com.tuotiansudai.rest.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RestHandlerExceptionResolver extends AbstractHandlerExceptionResolver {
    private static Logger logger = LoggerFactory.getLogger(RestHandlerExceptionResolver.class);

    public RestHandlerExceptionResolver() {
        setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
    }

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        if (ex instanceof RestException) {
            logger.warn(String.format("exception on %s : %s", request.getRequestURI(), ex.getMessage()), ex);
        } else {
            logger.error("unhandled exception on " + request.getRequestURI(), ex);
        }

        if (WebUtils.printIfFirstAcceptJson(request, response, ex)) {
            return new ModelAndView();
        } else {
            return null;
        }
    }
}
