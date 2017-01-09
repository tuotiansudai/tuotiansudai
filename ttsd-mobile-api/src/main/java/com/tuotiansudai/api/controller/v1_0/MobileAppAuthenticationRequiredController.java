package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.anxin.service.AnxinSignService;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppActivityService;
import com.tuotiansudai.spring.LoginUserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(description = "是否需要认证校验")
public class MobileAppAuthenticationRequiredController extends MobileAppBaseController {

    private final AnxinSignService anxinSignService;

    @Autowired
    public MobileAppAuthenticationRequiredController(AnxinSignService anxinSignService) {
        this.anxinSignService = anxinSignService;
    }

    @RequestMapping(value = "/get/is-authentication-required", method = RequestMethod.POST)
    @ApiOperation("获取认证")
    public BaseResponseDto<AuthenticationRequiredResponseDto> isAuthenticationRequired() {
        BaseResponseDto<AuthenticationRequiredResponseDto> baseResponseDto = new BaseResponseDto<>();
        baseResponseDto.setData(new AuthenticationRequiredResponseDto(anxinSignService.isAuthenticationRequired(LoginUserInfo.getLoginName())));
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseResponseDto;
    }
}