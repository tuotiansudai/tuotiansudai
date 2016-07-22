package com.tuotiansudai.point.dto;


import com.tuotiansudai.point.repository.model.PointTask;

public class PointTaskDto {

    private PointTask name;

    private String title;

    private String description;

    private long point;

    private boolean completed;

    private String url;

    public PointTask getName() {
        return name;
    }

    public void setName(PointTask name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
