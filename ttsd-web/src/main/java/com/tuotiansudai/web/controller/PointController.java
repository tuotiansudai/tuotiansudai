package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.point.dto.SignInPointDto;
import com.tuotiansudai.point.service.PointService;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/point")
public class PointController {
    @Autowired
    private PointService pointService;

    @RequestMapping(path = "/sign-in", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> signIn() {
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto baseDataDto = new SignInPointDto();
        baseDto.setData(baseDataDto);
        String loginName = LoginUserInfo.getLoginName();
        boolean signInIsSuccess = pointService.signInIsSuccess(loginName);
        if (signInIsSuccess){
            baseDataDto.setStatus(false);
            baseDataDto.setMessage("今天已经成功签到！");
            baseDto.setSuccess(true);
            return baseDto;
        }
        baseDataDto = pointService.signIn(loginName);
        baseDataDto.setStatus(true);
        baseDto.setData(baseDataDto);
        baseDto.setSuccess(true);
        return baseDto;
    }
}
