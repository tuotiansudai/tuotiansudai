package com.tuotiansudai.point.repository.model;

import java.io.Serializable;
import java.util.Date;

public class PointTaskModel implements Serializable {

    private long id;

    private PointTask name;

    private long point;

    private Date createdTime;
    private boolean active;
    private boolean multiple;

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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }
}
