package com.tuotiansudai.service.impl;

import com.tuotiansudai.dto.RetrievePasswordDto;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.CaptchaType;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.RetrievePasswordService;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.util.MyShaPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RetrievePasswordServiceImpl implements RetrievePasswordService {

    @Autowired
    private SmsCaptchaService smsCaptchaService;

    @Autowired
    private MyShaPasswordEncoder myShaPasswordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public boolean mobileRetrievePassword(RetrievePasswordDto retrievePasswordDto) {
        String mobile = retrievePasswordDto.getMobile();
        String captcha = retrievePasswordDto.getCaptcha();
        String password = retrievePasswordDto.getPassword();
        UserModel userModel = userMapper.findByMobile(mobile);
        if (userModel != null && smsCaptchaService.verifyMobileCaptcha(mobile, captcha, CaptchaType.RETRIEVE_PASSWORD_CAPTCHA)) {
            String encodePassword = myShaPasswordEncoder.encodePassword(password, userModel.getSalt());
            userMapper.updatePasswordByLoginName(userModel.getLoginName(), encodePassword);
            return true;
        }
        return false;
    }
}
