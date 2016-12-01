package com.tuotiansudai.api.dto;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDataDto;
import com.tuotiansudai.point.repository.dto.PointTaskDto;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class TaskCenterResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "新手任务", example = "list")
    private List<PointTaskDto> newbieTasks;

    @ApiModelProperty(value = "高级任务", example = "list")
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
