package com.tuotiansudai.point.service;

import com.tuotiansudai.point.dto.PointTaskDto;
import com.tuotiansudai.point.repository.model.PointTask;
import com.tuotiansudai.repository.model.InvestModel;

import java.util.List;

public interface PointTaskService {

    void completeNewbieTask(PointTask pointTask, String loginName);

    void completeAdvancedTask(PointTask pointTask, String loginName);

    List<PointTaskDto> getNewbiePointTasks(String loginName);

    List<PointTaskDto> getAdvancedPointTasks(String loginName);

    List<PointTaskDto> getCompletedAdvancedPointTasks(String loginName);
}
