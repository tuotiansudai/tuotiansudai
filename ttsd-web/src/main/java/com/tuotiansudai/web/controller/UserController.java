package com.tuotiansudai.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.web.common.CommonConstants;
import com.tuotiansudai.web.dto.Data;
import com.tuotiansudai.web.dto.UserInteractiveDto;
import com.tuotiansudai.web.dto.UserInteractiveJsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @JsonView(UserInteractiveJsonView.UserInteractive.class)
    @RequestMapping(value = "/register/email/{email}/verify", method = RequestMethod.GET)
    public UserInteractiveDto jsonEmailIsExisted(@PathVariable String email) {
        UserInteractiveDto userInteractiveDto = new UserInteractiveDto();
        Data data = new Data();
        try {
            boolean isExistEmail = userService.userEmailIsExisted(email);
            String status = CommonConstants.FAIL_STATUS;
            if (isExistEmail) {
                status = CommonConstants.SUCCESS_STATUS;
            }
            data.setExist(isExistEmail);
            userInteractiveDto.setStatus(status);
            userInteractiveDto.setData(data);
        } catch (Exception e) {
            data.setExist(false);
            userInteractiveDto.setStatus(CommonConstants.FAIL_STATUS);
            userInteractiveDto.setData(data);
            e.printStackTrace();
        }
        return userInteractiveDto;

    }

    @JsonView(UserInteractiveJsonView.UserInteractive.class)
    @RequestMapping(value = "/register/mobileNumber/{mobileNumber}/verify", method = RequestMethod.GET)
    public UserInteractiveDto jsonMobileNumberIsExisted(@PathVariable String mobileNumber) {
        UserInteractiveDto userInteractiveDto = new UserInteractiveDto();
        Data data = new Data();
        try {
            boolean isExistedEmail = userService.userMobileNumberIsExisted(mobileNumber);
            String status = CommonConstants.FAIL_STATUS;
            if (isExistedEmail) {
                status = CommonConstants.SUCCESS_STATUS;
            }
            data.setExist(isExistedEmail);
            userInteractiveDto.setStatus(status);
            userInteractiveDto.setData(data);
        } catch (Exception e) {
            data.setExist(false);
            userInteractiveDto.setStatus(CommonConstants.FAIL_STATUS);
            userInteractiveDto.setData(data);
            e.printStackTrace();
        }
        return userInteractiveDto;

    }

    @JsonView(UserInteractiveJsonView.UserInteractive.class)
    @RequestMapping(value = "/register/referrer/{referrer}/verify", method = RequestMethod.GET)
    public UserInteractiveDto jsonReferrerIsExisted(@PathVariable String referrer) {
        UserInteractiveDto userInteractiveDto = new UserInteractiveDto();
        Data data = new Data();
        try {
            boolean isExistedEmail = userService.referrerIsExisted(referrer);
            String status = CommonConstants.FAIL_STATUS;
            if (isExistedEmail) {
                status = CommonConstants.SUCCESS_STATUS;
            }
            data.setExist(isExistedEmail);
            userInteractiveDto.setStatus(status);
            userInteractiveDto.setData(data);
        } catch (Exception e) {
            data.setExist(false);
            userInteractiveDto.setStatus(CommonConstants.FAIL_STATUS);
            userInteractiveDto.setData(data);
            e.printStackTrace();
        }
        return userInteractiveDto;

    }

}
