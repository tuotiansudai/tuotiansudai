package com.tuotiansudai.dto;

import com.google.common.collect.Lists;

import java.util.List;

public class MonitorDataDto extends BaseDataDto {

    private String service;

    private boolean databaseStatus;

    private boolean redisStatus;

    private List<MonitorDataDto> dependence = Lists.newArrayList();

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public boolean isDatabaseStatus() {
        return databaseStatus;
    }

    public void setDatabaseStatus(boolean databaseStatus) {
        this.databaseStatus = databaseStatus;
    }

    public boolean isRedisStatus() {
        return redisStatus;
    }

    public void setRedisStatus(boolean redisStatus) {
        this.redisStatus = redisStatus;
    }

    public List<MonitorDataDto> getDependence() {
        return dependence;
    }

    public void setDependence(List<MonitorDataDto> dependence) {
        this.dependence = dependence;
    }
}
