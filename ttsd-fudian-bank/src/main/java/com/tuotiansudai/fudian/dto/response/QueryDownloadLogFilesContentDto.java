package com.tuotiansudai.fudian.dto.response;


public class QueryDownloadLogFilesContentDto extends BaseContentDto{

    private String sftpFilePath;

    private String filename;

    private String type;

    public String getSftpFilePath() {
        return sftpFilePath;
    }

    public void setSftpFilePath(String sftpFilePath) {
        this.sftpFilePath = sftpFilePath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
