package com.tuotiansudai.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.web.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @JsonView(RegisterVerifyJsonView.RegisterVerify.class)
    @RequestMapping(value = "/register/email/{email}/verify", method = RequestMethod.GET)
    @ResponseBody
    public RegisterVerifyDto jsonEmailIsExisted(@PathVariable String email) {
        RegisterVerifyDto registerVerifyDto = new RegisterVerifyDto();
        Data data = new Data();
        try {
            boolean isExistEmail = userService.userEmailIsExisted(email);
            RegisterVerificationStatus status = RegisterVerificationStatus.FAIL;
            if (isExistEmail) {
                status = RegisterVerificationStatus.SUCCESS;
            }
            data.setExist(isExistEmail);
            registerVerifyDto.setStatus(status);
            registerVerifyDto.setData(data);
        } catch (Exception e) {
            data.setExist(false);
            registerVerifyDto.setStatus(RegisterVerificationStatus.FAIL);
            registerVerifyDto.setData(data);
            e.printStackTrace();
        }

        return registerVerifyDto;


    }

    @JsonView(RegisterVerifyJsonView.RegisterVerify.class)
    @RequestMapping(value = "/register/mobileNumber/{mobileNumber}/verify", method = RequestMethod.GET)
    public RegisterVerifyDto jsonMobileNumberIsExisted(@PathVariable String mobileNumber) {
        RegisterVerifyDto registerVerifyDto = new RegisterVerifyDto();
        Data data = new Data();
        try {
            boolean isExistedEmail = userService.userMobileNumberIsExisted(mobileNumber);
            RegisterVerificationStatus status = RegisterVerificationStatus.FAIL;
            if (isExistedEmail) {
                status = RegisterVerificationStatus.SUCCESS;
            }
            data.setExist(isExistedEmail);
            registerVerifyDto.setStatus(status);
            registerVerifyDto.setData(data);
        } catch (Exception e) {
            data.setExist(false);
            registerVerifyDto.setStatus(RegisterVerificationStatus.SUCCESS);
            registerVerifyDto.setData(data);
            e.printStackTrace();
        }
        return registerVerifyDto;

    }
    @JsonView(RegisterVerifyJsonView.RegisterVerify.class)
    @RequestMapping(value = "/register/referrer/{referrer}/verify", method = RequestMethod.GET)
    public RegisterVerifyDto jsonReferrerIsExisted(@PathVariable String referrer) {
        RegisterVerifyDto registerVerifyDto = new RegisterVerifyDto();
        Data data = new Data();
        try {
            boolean isExistedEmail = userService.referrerIsExisted(referrer);
            RegisterVerificationStatus status = RegisterVerificationStatus.FAIL;
            if (isExistedEmail) {
                status = RegisterVerificationStatus.SUCCESS;
            }
            data.setExist(isExistedEmail);
            registerVerifyDto.setStatus(status);
            registerVerifyDto.setData(data);
        } catch (Exception e) {
            data.setExist(false);
            registerVerifyDto.setStatus(RegisterVerificationStatus.FAIL);
            registerVerifyDto.setData(data);
            e.printStackTrace();
        }
        return registerVerifyDto;

    }

    @RequestMapping(value = "/register/insertUser", method = RequestMethod.POST)
    public @ResponseBody JsonDto jsonRegisterUser(@ModelAttribute("userModel") UserModel userModel){
        JsonDto jsonDto = new JsonDto();
        try {
            this.userService.registerUser(userModel);
            jsonDto.setStatus(RegisterVerificationStatus.SUCCESS.toString());
        } catch (Exception e){
            jsonDto.setStatus(RegisterVerificationStatus.FAIL.toString());
            e.getStackTrace();
            e.printStackTrace();
        }
        jsonDto.setModel(userModel);
        return jsonDto;
    }

}
