package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.RetrievePasswordRequestDto;
import com.tuotiansudai.api.service.MobileAppRetrievePasswordService;
import com.tuotiansudai.api.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class MobileAppRetrievePasswordController extends MobileAppBaseController {

    @Autowired
    private MobileAppRetrievePasswordService retrievePasswordService;

    /**
     * @param retrievePasswordRequestDto
     * @return BaseResponseDto
     * @function 找回密码
     */
    @RequestMapping(value = "/retrievepassword", method = RequestMethod.POST)
    public BaseResponseDto retrievePassword(@Valid @RequestBody RetrievePasswordRequestDto retrievePasswordRequestDto) {
        return retrievePasswordService.retrievePassword(retrievePasswordRequestDto);
    }

    /**
     * @param retrievePasswordRequestDto
     * @return BaseResponseDto
     * @function 校验手机验证码是否正确
     */
    @RequestMapping(value = "/validatecaptcha", method = RequestMethod.POST)
    public BaseResponseDto validateAuthCode(@RequestBody RetrievePasswordRequestDto retrievePasswordRequestDto) {
        return retrievePasswordService.validateAuthCode(retrievePasswordRequestDto);
    }

    /**
     * @param retrievePasswordRequestDto
     * @param request
     * @return BaseResponseDto
     * @function 发送手机验证码
     */
    @RequestMapping(value = "/retrievepassword/sendsms", method = RequestMethod.POST)
    public BaseResponseDto sendSMS(@RequestBody RetrievePasswordRequestDto retrievePasswordRequestDto, HttpServletRequest request) {
        return retrievePasswordService.sendSMS(retrievePasswordRequestDto, CommonUtils.getRemoteHost(request));
    }
}
