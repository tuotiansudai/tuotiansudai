package com.tuotiansudai.rest.handler;

import com.tuotiansudai.rest.exceptions.RestException;
import com.tuotiansudai.rest.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RestStatusHandlerExceptionResolver extends ResponseStatusExceptionResolver {
    private static Logger logger = LoggerFactory.getLogger(RestStatusHandlerExceptionResolver.class);

    public RestStatusHandlerExceptionResolver() {
        setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Override
    protected ModelAndView resolveResponseStatus(ResponseStatus responseStatus, HttpServletRequest request,
                                                 HttpServletResponse response, Object handler, Exception ex) throws Exception {
        response.setStatus(responseStatus.code().value());
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
