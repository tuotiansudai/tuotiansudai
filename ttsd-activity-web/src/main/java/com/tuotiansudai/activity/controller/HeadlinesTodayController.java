package com.tuotiansudai.activity.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.activity.repository.dto.DrawLotteryResultDto;
import com.tuotiansudai.activity.service.HeadlinesTodayPrizeService;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.ReferrerRelationException;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.spring.security.MyAuthenticationUtil;
import com.tuotiansudai.util.IdentityNumberValidator;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.MessageFormat;

@Controller
@RequestMapping(path = "/headlines-today")
public class HeadlinesTodayController {
    @Autowired
    private HeadlinesTodayPrizeService headlinesTodayPrizeService;

    @Autowired
    private UserService userService;

    @Autowired
    private MyAuthenticationUtil myAuthenticationUtil;

    private final static Logger logger = Logger.getLogger(HeadlinesTodayController.class);


    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView headlinesToday() {
        ModelAndView modelAndView = new ModelAndView("/activities/headlines-today", "responsive", true);
        modelAndView.addObject("activityType","national");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/draw", method = RequestMethod.POST)
    public DrawLotteryResultDto headlinesTodayDrawPrize(@RequestParam(value = "mobile", required = false) String mobile) {
        return headlinesTodayPrizeService.drawLotteryPrize(Strings.isNullOrEmpty(LoginUserInfo.getMobile()) ? mobile : LoginUserInfo.getMobile());
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> registerUserNoRedirect(@Valid @ModelAttribute RegisterUserDto registerUserDto, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        BaseDataDto dataDto = new BaseDataDto();
        boolean isRegisterSuccess = false;
        try {
            if (request.getSession().getAttribute("channel") != null) {
                registerUserDto.setChannel(String.valueOf(request.getSession().getAttribute("channel")));
            }
            logger.info(MessageFormat.format("[Register User {0}] controller starting...", registerUserDto.getMobile()));
            isRegisterSuccess = this.userService.registerUser(registerUserDto);
            logger.info(MessageFormat.format("[Register User {0}] controller invoked service ({0})", registerUserDto.getMobile(), String.valueOf(isRegisterSuccess)));
        } catch (ReferrerRelationException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        if (!isRegisterSuccess) {
            redirectAttributes.addFlashAttribute("originalFormData", registerUserDto);
            redirectAttributes.addFlashAttribute("success", false);
        }

        if (isRegisterSuccess) {
            logger.info(MessageFormat.format("[Register User {0}] authenticate starting...", registerUserDto.getMobile()));
            myAuthenticationUtil.createAuthentication(registerUserDto.getMobile(), Source.WEB);
            dataDto.setStatus(true);
            logger.info(MessageFormat.format("[Register User {0}] authenticate completed", registerUserDto.getMobile()));
        }

        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        baseDto.setData(dataDto);

        return baseDto;
    }

    @RequestMapping(value="/account", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> registerAccountNoRedirect(@Valid @ModelAttribute RegisterAccountDto registerAccountDto) throws Exception {
        if (IdentityNumberValidator.validateIdentity(registerAccountDto.getIdentityNumber())) {
            registerAccountDto.setLoginName(LoginUserInfo.getLoginName());
            registerAccountDto.setMobile(LoginUserInfo.getMobile());
            BaseDto<PayDataDto> baseDto = this.userService.registerAccount(registerAccountDto);
            myAuthenticationUtil.createAuthentication(LoginUserInfo.getLoginName(), Source.WEB);
            return baseDto;
        }

        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        baseDto.setData(dataDto);
        return baseDto;
    }








}
