package com.tuotiansudai.web.controller;


import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.web.dto.ResultDataDto;
import com.tuotiansudai.web.dto.ResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/register")
public class RegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private SmsCaptchaService smsCaptchaService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView register() {
        return new ModelAndView("/register");
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

    @RequestMapping(value = "/loginName/{loginName}/verify", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto loginNameIsExisted(@PathVariable String loginName) {
        boolean isExist = userService.loginNameIsExisted(loginName);

        BaseDto registerVerifyDto = new BaseDto();
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(isExist);
        registerVerifyDto.setData(dataDto);
        return registerVerifyDto;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public BaseDto registerUser(@RequestBody UserModel userModel) {
        boolean success = this.userService.registerUser(userModel);

        BaseDto registerResultDto = new BaseDto();
        BaseDataDto dataDto = new BaseDataDto();
        registerResultDto.setData(dataDto);
        dataDto.setStatus(success);

        return registerResultDto;
    }

    @RequestMapping(value = "/mobile/{mobile}/sendCaptcha", method = RequestMethod.GET)
    @ResponseBody
    public ResultDto sendRegisterByMobileNumberSMS(@PathVariable String mobile) {
        ResultDto resultDto = new ResultDto();
        ResultDataDto data = new ResultDataDto();
        try {
            data.setStatus(smsCaptchaService.sendSmsByMobileNumberRegister(mobile));
        } catch (Exception e) {
            data.setStatus(false);
            e.printStackTrace();
        }
        resultDto.setSuccess(true);
        resultDto.setData(data);
        return resultDto;
    }

    @RequestMapping(value = "/mobile/{mobile}/captcha/{captcha}/verify", method = RequestMethod.GET)
    @ResponseBody
    public ResultDto verifyCaptchaIsValid(@PathVariable String mobile, @PathVariable String captcha) {
        ResultDto resultDto = new ResultDto();

        ResultDataDto data = new ResultDataDto();

        data.setStatus(smsCaptchaService.verifyCaptcha(mobile, captcha));

        resultDto.setSuccess(true);

        resultDto.setData(data);

        return resultDto;

    }

}
