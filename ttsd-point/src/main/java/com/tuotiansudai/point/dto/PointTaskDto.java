package com.tuotiansudai.point.dto;


import com.tuotiansudai.point.repository.model.PointTask;
import com.tuotiansudai.point.repository.model.PointTaskModel;

public class PointTaskDto {
    private long id;
    private PointTask name;
    private long point;
    private boolean completed;

    public PointTaskDto(){

    }
    public PointTaskDto(PointTaskModel pointTaskModel){
        this.id = pointTaskModel.getId();
        this.point = pointTaskModel.getPoint();
        this.name = pointTaskModel.getName();
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PointTask getName() {
        return name;
    }

    public void setName(PointTask name) {
        this.name = name;
    }

    public long getPoint() {
        return point;
    }

    public void setPoint(long point) {
        this.point = point;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
