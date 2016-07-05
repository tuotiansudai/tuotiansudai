package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.TaskCenterResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.service.MobileAppTaskCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/task-center")
public class MobileAppTaskCenterController {

    @Autowired
    private MobileAppTaskCenterService mobileAppTaskCenterService;

    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponseDto<TaskCenterResponseDataDto> getTasks() {
        return mobileAppTaskCenterService.getTasks("gaoyinglong");
    }

    @RequestMapping(value = "/completed-tasks", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponseDto<TaskCenterResponseDataDto> getCompletedTasks() {
        return mobileAppTaskCenterService.getCompletedTasks("gaoyinglong");
    }
}
