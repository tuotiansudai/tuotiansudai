package com.tuotiansudai.fudian.dto.request;


import org.joda.time.DateTime;

import java.util.Date;

public class QueryDownloadLogFiles extends BaseRequestDto{

    private String queryDate;

    private String type;

    public QueryDownloadLogFiles(String type) {
        super(Source.WEB, null, null, null, null);
        this.queryDate = "20180608";
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
