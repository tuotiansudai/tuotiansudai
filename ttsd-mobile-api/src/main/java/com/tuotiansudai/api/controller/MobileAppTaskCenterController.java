package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.TaskCenterResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.service.MobileAppTaskCenterService;
import com.tuotiansudai.spring.LoginUserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/task-center")
@Api(description = "任务中心")
public class MobileAppTaskCenterController {

    @Autowired
    private MobileAppTaskCenterService mobileAppTaskCenterService;

    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("列表")
    public BaseResponseDto<TaskCenterResponseDataDto> getTasks() {
        return mobileAppTaskCenterService.getTasks(LoginUserInfo.getLoginName());
    }

    @RequestMapping(value = "/completed-tasks", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("完成任务")
    public BaseResponseDto<TaskCenterResponseDataDto> getCompletedTasks() {
        return mobileAppTaskCenterService.getCompletedTasks(LoginUserInfo.getLoginName());
    }
}
