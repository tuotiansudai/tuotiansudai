package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanApplicationDto;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.LoanApplicationRegion;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.LoanApplicationService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/loan-application")
public class LoanApplicationController {

    @Autowired
    LoanApplicationService loanApplicationService;

    @Autowired
    UserService userService;

    @Autowired
    AccountService accountService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();

        String mobile = LoginUserInfo.getMobile();
        if (!StringUtils.isEmpty(mobile)) {
            modelAndView.addObject("mobile", mobile);
            UserModel userModel = userService.findByMobile(mobile);
            AccountModel accountModel = accountService.findByLoginName(userModel.getLoginName());
            if (null != accountModel) {
                modelAndView.addObject("userName", accountModel.getUserName());
            }
        }

        modelAndView.addObject("regions", LoanApplicationRegion.values());

        return modelAndView;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> create(@RequestBody LoanApplicationDto loanApplicationDto) {
        String loginName = LoginUserInfo.getLoginName();
        if (StringUtils.isEmpty(loginName)) {
            return new BaseDto<>(new BaseDataDto(false, "未登录"));
        }
        loanApplicationDto.setLoginName(loginName);
        return loanApplicationService.create(loanApplicationDto);
    }
}
