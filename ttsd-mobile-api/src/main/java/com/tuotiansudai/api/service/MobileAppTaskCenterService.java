package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.TaskCenterResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;

public interface MobileAppTaskCenterService {

    BaseResponseDto<TaskCenterResponseDataDto> getTasks(String loginName);

    BaseResponseDto<TaskCenterResponseDataDto> getCompletedTasks(String loginName);
}
