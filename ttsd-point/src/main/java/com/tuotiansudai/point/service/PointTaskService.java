package com.tuotiansudai.point.service;

import com.tuotiansudai.point.dto.PointTaskDto;
import com.tuotiansudai.point.repository.model.PointTask;
import com.tuotiansudai.repository.model.InvestModel;

import java.util.List;

public interface PointTaskService {

    void completeNewbieTask(PointTask pointTask, String loginName);

    void completeAdvancedTask(PointTask pointTask, String loginName);

    List<PointTaskDto> displayPointTask(int index,int pageSize,String loginName);

}
