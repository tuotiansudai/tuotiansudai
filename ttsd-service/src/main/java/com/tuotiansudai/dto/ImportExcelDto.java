package com.tuotiansudai.dto;


import java.io.Serializable;
import java.util.List;

public class ImportExcelDto implements Serializable{

    private boolean status;

    private String fileUuid;

    private Integer totalCount;

    private List<String> successLoginNames;

    private String message;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getFileUuid() {
        return fileUuid;
    }

    public void setFileUuid(String fileUuid) {
        this.fileUuid = fileUuid;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<String> getSuccessLoginNames() {
        return successLoginNames;
    }

    public void setSuccessLoginNames(List<String> successLoginNames) {
        this.successLoginNames = successLoginNames;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
