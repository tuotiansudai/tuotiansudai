package com.tuotiansudai.service.impl;

import com.tuotiansudai.dto.RetrievePasswordDto;
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
    public boolean mobileRetrievePassword(RetrievePasswordDto retrievePasswordDto) {
        String mobile = retrievePasswordDto.getMobile();
        String captcha = retrievePasswordDto.getCaptcha();
        String password = retrievePasswordDto.getPassword();
        if (userService.mobileIsExist(mobile) && smsCaptchaService.verifyMobileCaptcha(mobile,captcha)){
            String salt = myShaPasswordEncoder.generateSalt();
            String encodePassword = myShaPasswordEncoder.encodePassword(password, salt);
            userService.updatePassword(mobile,encodePassword);
            return true;
        }
        return false;
    }
}
