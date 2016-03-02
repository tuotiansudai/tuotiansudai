package com.tuotiansudai.point.service;

import com.tuotiansudai.point.dto.PointTaskDto;
import com.tuotiansudai.point.repository.model.PointTask;

import java.util.List;

public interface PointTaskService {

    void completeTask(PointTask pointTask, String loginName);

    List<PointTaskDto> displayPointTask(int index,int pageSize,String loginName);

}
