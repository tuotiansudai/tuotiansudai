package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.RetrievePasswordRequestDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppRetrievePasswordService;
import com.tuotiansudai.dto.RetrievePasswordDto;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.CaptchaType;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.util.MyShaPasswordEncoder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileAppRetrievePasswordServiceImpl implements MobileAppRetrievePasswordService {

    static Logger log = Logger.getLogger(MobileAppRetrievePasswordServiceImpl.class);

    @Autowired
    private SmsCaptchaService smsCaptchaService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MyShaPasswordEncoder myShaPasswordEncoder;

    @Override
    public BaseResponseDto retrievePassword(RetrievePasswordRequestDto retrievePasswordRequestDto) {
        RetrievePasswordDto retrievePasswordDto = retrievePasswordRequestDto.convertToRetrievePasswordDto();
        String mobile = retrievePasswordDto.getMobile();
        String captcha = retrievePasswordDto.getCaptcha();
        String password = retrievePasswordDto.getPassword();
        boolean verified = smsCaptchaService.verifyMobileCaptcha(mobile, captcha, CaptchaType.RETRIEVE_PASSWORD_CAPTCHA);
        if(verified){
            UserModel userModel = userMapper.findByMobile(mobile);
            if(userModel != null){
                userMapper.updatePassword(userModel.getLoginName(), myShaPasswordEncoder.encodePassword(password, userModel.getSalt()));
                BaseResponseDto baseResponseDto = new BaseResponseDto();
                baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
                baseResponseDto.setMessage("");
                return baseResponseDto;
            }else{
                return new BaseResponseDto(ReturnMessage.SMS_CAPTCHA_ERROR.getCode(),ReturnMessage.SMS_CAPTCHA_ERROR.getMsg());
            }
        }else{
            return new BaseResponseDto(ReturnMessage.SMS_CAPTCHA_ERROR.getCode(),ReturnMessage.SMS_CAPTCHA_ERROR.getMsg());
        }
    }

}
