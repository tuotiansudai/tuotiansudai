package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppRegisterService;
import com.tuotiansudai.api.util.CommonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@Api(description = "用户推荐的统计")
public class MobileAppRegisterController extends MobileAppBaseController {

    @Autowired
    private MobileAppRegisterService mobileAppRegisterService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ApiOperation("用户推荐的统计")
    public BaseResponseDto<RegisterResponseDataDto> registerUser(@Valid @RequestBody RegisterRequestDto registerRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorList = (List) bindingResult.getAllErrors();
            //当验证的用户名和密码同时错误时
            if (errorList.size() > 1) {
                return new BaseResponseDto(ReturnMessage.USER_NAME_OR_PASSWORD_IS_INVALID.getCode(), ReturnMessage.USER_NAME_OR_PASSWORD_IS_INVALID.getMsg());
            } else {
                String errorCode = bindingResult.getFieldError().getDefaultMessage();
                String errorMessage = ReturnMessage.getErrorMsgByCode(errorCode);
                return new BaseResponseDto(errorCode, errorMessage);
            }
        } else {
            return mobileAppRegisterService.registerUser(registerRequestDto);
        }
    }

    @RequestMapping(value = "/register-huizu", method = RequestMethod.POST)
    @ApiOperation("注册慧租用户")
    public BaseResponseDto<RegisterResponseDataDto> registerUserHuizu(@Valid @RequestBody RegisterHuizuRequestDto registerRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorCode = bindingResult.getFieldError().getDefaultMessage();
            String errorMessage = ReturnMessage.getErrorMsgByCode(errorCode);
            return new BaseResponseDto(errorCode, errorMessage);
        } else {
            return mobileAppRegisterService.registerUserFromHuizu(registerRequestDto);
        }
    }

    @RequestMapping(value = "/register/sendsms", method = RequestMethod.POST)
    @ApiOperation("发送验证码")
    public BaseResponseDto sendRegisterByMobileNumberSMS(@RequestBody SendSmsRequestDto sendSmsRequestDto, HttpServletRequest request) {
        String mobileNumber = sendSmsRequestDto.getPhoneNum();
        String remoteIp = CommonUtils.getRemoteHost(request);
        return mobileAppRegisterService.sendRegisterByMobileNumberSMS(mobileNumber, remoteIp);
    }

    @RequestMapping(value = "/get/mobile-is-available", method = RequestMethod.POST)
    @ApiOperation("验证手机号是否已注册")
    public BaseResponseDto mobileNumberExist(@RequestBody MobileIsAvailableRequestDto requestDto) {
        return mobileAppRegisterService.mobileNumberIsExist(requestDto);
    }
}
