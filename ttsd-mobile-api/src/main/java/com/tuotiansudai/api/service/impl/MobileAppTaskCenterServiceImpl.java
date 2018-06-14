package com.tuotiansudai.api.service.impl;


import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.tuotiansudai.api.dto.TaskCenterResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppTaskCenterService;
import com.tuotiansudai.point.repository.dto.PointTaskDto;
import com.tuotiansudai.point.service.PointTaskService;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MobileAppTaskCenterServiceImpl implements MobileAppTaskCenterService {

    @Autowired
    private BankAccountMapper bankAccountMapper;

    @Autowired
    private PointTaskService pointTaskService;

    @Override
    public BaseResponseDto<TaskCenterResponseDataDto> getTasks(String loginName) {
        BaseResponseDto<TaskCenterResponseDataDto> responseDto = new BaseResponseDto<>();

        TaskCenterResponseDataDto data = new TaskCenterResponseDataDto();

        List<PointTaskDto> newbiePointTasks = pointTaskService.getNewbiePointTasks(loginName);

        if (Iterators.any(newbiePointTasks.iterator(), dto -> !dto.isCompleted())) {
            data.setNewbieTasks(newbiePointTasks);
        }

        if (bankAccountMapper.findByLoginName(loginName) != null) {
            data.setAdvancedTasks(pointTaskService.getAdvancedPointTasks(loginName));
        }

        responseDto.setData(data);
        responseDto.setCode(ReturnMessage.SUCCESS.getCode());
        responseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return responseDto;
    }

    @Override
    public BaseResponseDto<TaskCenterResponseDataDto> getCompletedTasks(String loginName) {
        BaseResponseDto<TaskCenterResponseDataDto> responseDto = new BaseResponseDto<>();

        TaskCenterResponseDataDto data = new TaskCenterResponseDataDto();

        List<PointTaskDto> newbiePointTasks = pointTaskService.getNewbiePointTasks(loginName);

        if (Iterators.all(newbiePointTasks.iterator(), new Predicate<PointTaskDto>() {
            @Override
            public boolean apply(PointTaskDto dto) {
                return dto.isCompleted();
            }
        })) {
            data.setNewbieTasks(newbiePointTasks);
        }

        if (bankAccountMapper.findByLoginName(loginName) != null) {
            data.setAdvancedTasks(pointTaskService.getCompletedAdvancedPointTasks(loginName));
        }


        responseDto.setData(data);
        responseDto.setCode(ReturnMessage.SUCCESS.getCode());
        responseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return responseDto;
    }
}
