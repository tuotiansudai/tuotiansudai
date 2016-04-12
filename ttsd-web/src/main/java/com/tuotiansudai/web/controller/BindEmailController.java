package com.tuotiansudai.web.controller;


import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.service.BindEmailService;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/bind-email")
public class BindEmailController {

    @Autowired
    private BindEmailService bindEmailService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> bindEmail(HttpServletRequest request) {
        String email = request.getParameter("email");
        String url = request.getRequestURL().toString();
        boolean result = bindEmailService.sendActiveEmail(LoginUserInfo.getLoginName(), email, url);
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(result);
        baseDto.setData(dataDto);
        return baseDto;
    }

    @RequestMapping(value = "/verify/{sign}", method = RequestMethod.GET)
    public ModelAndView verifyEmail(@PathVariable String sign) {
        return new ModelAndView("/bind-email", "email", bindEmailService.verifyEmail(LoginUserInfo.getLoginName(), sign));
    }


}
