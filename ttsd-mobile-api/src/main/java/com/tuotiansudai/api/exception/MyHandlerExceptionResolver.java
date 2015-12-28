package com.tuotiansudai.api.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
@Service
public class MyHandlerExceptionResolver implements HandlerExceptionResolver{
    static Logger log = Logger.getLogger(MyHandlerExceptionResolver.class);
    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        PrintWriter out = null;
        try {
            BaseResponseDto dto = new BaseResponseDto(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode(),ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
            out = response.getWriter();

            String jsonString = objectMapper.writeValueAsString(dto);
            out.print(jsonString);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return new ModelAndView();
    }
}
