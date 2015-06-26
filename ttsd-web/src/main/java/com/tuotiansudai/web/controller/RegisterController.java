package com.tuotiansudai.web.controller;


import com.tuotiansudai.service.UserService;
import com.tuotiansudai.web.dto.BaseDataDto;
import com.tuotiansudai.web.dto.BaseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/register")
public class RegisterController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView register() {
        return new ModelAndView("/view.register");
    }

    @RequestMapping(value = "/email/{email}/verify", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto emailIsExisted(@PathVariable String email) {
        boolean isExist = userService.userEmailIsExisted(email);

        BaseDto registerVerifyDto = new BaseDto();
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(isExist);
        registerVerifyDto.setData(dataDto);

        return registerVerifyDto;
    }

    @RequestMapping(value = "/mobileNumber/{mobileNumber}/verify", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto mobileNumberIsExisted(@PathVariable String mobileNumber) {
        boolean isExist = userService.userMobileNumberIsExisted(mobileNumber);

        BaseDto registerVerifyDto = new BaseDto();
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(isExist);
        registerVerifyDto.setData(dataDto);

        return registerVerifyDto;

    }

    @RequestMapping(value = "/referrer/{referrer}/verify", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto referrerIsExisted(@PathVariable String referrer) {
        boolean isExist = userService.referrerIsExisted(referrer);

        BaseDto registerVerifyDto = new BaseDto();
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(isExist);
        registerVerifyDto.setData(dataDto);
        return registerVerifyDto;
    }

}
