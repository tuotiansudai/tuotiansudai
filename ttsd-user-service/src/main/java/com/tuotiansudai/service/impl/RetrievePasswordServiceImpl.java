package com.tuotiansudai.service.impl;

import com.tuotiansudai.dto.RetrievePasswordDto;
import com.tuotiansudai.dto.request.ResetPasswordRequestDto;
import com.tuotiansudai.enums.SmsCaptchaType;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.UserRestClient;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.RetrievePasswordService;
import com.tuotiansudai.service.SmsCaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RetrievePasswordServiceImpl implements RetrievePasswordService {

    @Autowired
    private SmsCaptchaService smsCaptchaService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRestClient userRestClient;

    @Override
    @Transactional
    public boolean mobileRetrievePassword(RetrievePasswordDto retrievePasswordDto) {
        String mobile = retrievePasswordDto.getMobile();
        String captcha = retrievePasswordDto.getCaptcha();
        String password = retrievePasswordDto.getPassword();
        UserModel userModel = userMapper.findByMobile(mobile);
        if (smsCaptchaService.verifyMobileCaptcha(mobile, captcha, SmsCaptchaType.RETRIEVE_PASSWORD_CAPTCHA)) {
            userRestClient.resetPassword(new ResetPasswordRequestDto(userModel.getLoginName(), password));
            return true;
        }
        return false;
    }
}
