package com.tuotiansudai.service.impl;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.service.RetrievePasswordService;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.utils.MyShaPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RetrievePasswordServiceImpl implements RetrievePasswordService{
    @Autowired
    private UserService userService;

    @Autowired
    private SmsCaptchaService smsCaptchaService;
    @Autowired
    private MyShaPasswordEncoder myShaPasswordEncoder;

    @Override
    public boolean mobileRetrievePassword(String mobile, String captcha, String password) {
        if (userService.mobileIsExist(mobile) && smsCaptchaService.verifyMobileCaptcha(mobile,captcha)){
            String salt = myShaPasswordEncoder.generateSalt();
            String encodePassword = myShaPasswordEncoder.encodePassword(password, salt);
            userService.updatePassword(mobile,encodePassword);
            return true;
        }
        return false;
    }
}
