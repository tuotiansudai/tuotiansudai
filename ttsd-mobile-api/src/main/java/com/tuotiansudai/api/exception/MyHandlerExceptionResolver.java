package com.tuotiansudai.api.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@Service
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {
    static Logger log = Logger.getLogger(MyHandlerExceptionResolver.class);
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.error(ex.getLocalizedMessage(), ex);

        try {
            if(!response.isCommitted()){
                response.reset();

                response.setContentType("application/json; charset=UTF-8");
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                BaseResponseDto dto = new BaseResponseDto(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode(), ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
                PrintWriter out = response.getWriter();

                String jsonString = objectMapper.writeValueAsString(dto);
                out.print(jsonString);

            }
        } catch (Exception e) {

            log.error(e.getLocalizedMessage(), e);
        }
        return new ModelAndView();
    }
}
