package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/register/account")
public class RegisterAccountController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView registerAccount() {
        return new ModelAndView("/register-account");
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView registerAccount(@Valid @ModelAttribute RegisterAccountDto registerAccountDto, RedirectAttributes redirectAttributes) {
        BaseDto<PayDataDto> dto = this.userService.registerAccount(registerAccountDto);
        boolean isRegisterSuccess = dto.getData().getStatus();
        if (!isRegisterSuccess) {
            redirectAttributes.addFlashAttribute("originalFormData", registerAccountDto);
            redirectAttributes.addFlashAttribute("success", isRegisterSuccess);
        }

        return new ModelAndView(isRegisterSuccess ? "redirect:/" : "redirect:/register/account");
    }
}
