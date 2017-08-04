package com.tuotiansudai.current.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tuotiansudai.repository.model.Source;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class TaskResponseDto {

    private long id;
    private Source source;

    @JsonProperty(value = "description")
    private String description;

    @JsonProperty(value = "created_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @JsonProperty(value = "status")
    private String status;

    @JsonProperty(value = "url")
    private String url;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
