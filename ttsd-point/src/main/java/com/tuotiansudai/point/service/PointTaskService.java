package com.tuotiansudai.point.service;

import com.tuotiansudai.point.repository.model.PointTask;

public interface PointTaskService {

    void completeTask(PointTask pointTask, String loginName);

}
