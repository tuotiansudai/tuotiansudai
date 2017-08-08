package com.tuotiansudai.dto;

import java.util.List;

public class CurrentDataDto<T> {

    private boolean success = true;

    private List<T> results;

    public CurrentDataDto() {
    }

    public CurrentDataDto(boolean success) {
        this.success = success;
    }

    public CurrentDataDto(List<T> results) {
        this.results = results;
    }

    public CurrentDataDto(boolean success, List<T> results) {
        this.success = success;
        this.results = results;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
