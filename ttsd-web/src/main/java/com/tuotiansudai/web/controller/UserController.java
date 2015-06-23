package com.tuotiansudai.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.web.dto.Data;
import com.tuotiansudai.web.dto.UserInteractiveDto;
import com.tuotiansudai.web.dto.UserInteractiveJsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @JsonView(UserInteractiveJsonView.UserInteractive.class)
    @RequestMapping(value = "/emailJson", method = RequestMethod.GET)
    public UserInteractiveDto jsonEmailExist(String email) {
        UserInteractiveDto userInteractiveDto = new UserInteractiveDto();
        Data data = new Data();
        try {
            boolean isExistEmai = userService.isExistEmail(email);
            String status = "fail";
            if (isExistEmai){
                status = "success";
            }
            data.setExist(isExistEmai);
            userInteractiveDto.setStatus(status);
            userInteractiveDto.setData(data);
        } catch (Exception e) {
            data.setExist(false);
            userInteractiveDto.setStatus("fail");
            userInteractiveDto.setData(data);
            e.printStackTrace();
        }
        return userInteractiveDto;

    }
    @JsonView(UserInteractiveJsonView.UserInteractive.class)
    @RequestMapping(value = "/mobileNumberJson", method = RequestMethod.GET)
    public UserInteractiveDto jsonMobileNumberExist(String mobileNumber) {
        UserInteractiveDto userInteractiveDto = new UserInteractiveDto();
        Data data = new Data();
        try {
            boolean isExistEmai = userService.isExistMobileNumber(mobileNumber);
            String status = "fail";
            if (isExistEmai){
                status = "success";
            }
            data.setExist(isExistEmai);
            userInteractiveDto.setStatus(status);
            userInteractiveDto.setData(data);
        } catch (Exception e) {
            data.setExist(false);
            userInteractiveDto.setStatus("fail");
            userInteractiveDto.setData(data);
            e.printStackTrace();
        }
        return userInteractiveDto;

    }
    @JsonView(UserInteractiveJsonView.UserInteractive.class)
    @RequestMapping(value = "/referrerJson", method = RequestMethod.GET)
    public UserInteractiveDto jsonReferrerExist(String referrer) {
        UserInteractiveDto userInteractiveDto = new UserInteractiveDto();
        Data data = new Data();
        try {
            boolean isExistEmai = userService.isExistReferrer(referrer);
            String status = "fail";
            if (isExistEmai){
                status = "success";
            }
            data.setExist(isExistEmai);
            userInteractiveDto.setStatus(status);
            userInteractiveDto.setData(data);
        } catch (Exception e) {
            data.setExist(false);
            userInteractiveDto.setStatus("fail");
            userInteractiveDto.setData(data);
            e.printStackTrace();
        }
        return userInteractiveDto;

    }

}
