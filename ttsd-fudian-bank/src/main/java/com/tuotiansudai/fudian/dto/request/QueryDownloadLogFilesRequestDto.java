package com.tuotiansudai.fudian.dto.request;


import org.joda.time.DateTime;

public class QueryDownloadLogFilesRequestDto extends BaseRequestDto{

    private String queryDate;

    private String type;

    public QueryDownloadLogFilesRequestDto(String type) {
        super(Source.WEB, null, null, null, null);
        this.queryDate = format.format(DateTime.now().minusDays(1).toDate());
        this.type = type;
    }

    public String getQueryDate() {
        return queryDate;
    }

    public void setQueryDate(String queryDate) {
        this.queryDate = queryDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
