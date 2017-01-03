package com.tuotiansudai.rest.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.rest.exceptions.RestExceptionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class WebUtils {
    private static Logger logger = LoggerFactory.getLogger(WebUtils.class);

    public static boolean printIfFirstAcceptJson(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(RestExceptionDto.fromException(ex));
            return printIfFirstAcceptJson(request, response, json);
        } catch (IOException e) {
            logger.warn("print error to client failed", e);
        }
        return false;
    }

    private static boolean printIfFirstAcceptJson(HttpServletRequest request, HttpServletResponse response, String json) {
        String requestAccept = request.getHeader("accept");
        if (requestAccept.startsWith("application/json") || requestAccept.startsWith("*/*")) {
            response.setContentType("application/json; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            try {
                PrintWriter writer = response.getWriter();
                writer.print(json);
                writer.close();
                return true;
            } catch (IOException e) {
                logger.warn("print error to client failed", e);
                return false;
            }
        }
        return false;
    }
}
