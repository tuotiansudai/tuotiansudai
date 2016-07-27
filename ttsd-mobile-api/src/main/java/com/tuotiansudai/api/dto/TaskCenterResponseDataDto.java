package com.tuotiansudai.api.dto;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDataDto;
import com.tuotiansudai.point.dto.PointTaskDto;

import java.util.List;

public class TaskCenterResponseDataDto extends BaseResponseDataDto {

    private List<PointTaskDto> newbieTasks;

    private List<PointTaskDto> advancedTasks;

    public List<PointTaskDto> getNewbieTasks() {
        return newbieTasks;
    }

    public void setNewbieTasks(List<PointTaskDto> newbieTasks) {
        this.newbieTasks = newbieTasks;
    }

    public List<PointTaskDto> getAdvancedTasks() {
        return advancedTasks;
    }

    public void setAdvancedTasks(List<PointTaskDto> advancedTasks) {
        this.advancedTasks = advancedTasks;
    }
}
