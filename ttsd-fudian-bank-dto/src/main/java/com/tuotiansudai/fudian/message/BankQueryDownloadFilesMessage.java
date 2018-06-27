package com.tuotiansudai.fudian.message;


import com.tuotiansudai.fudian.download.QueryDownloadLogFilesType;

import java.util.List;

public class BankQueryDownloadFilesMessage<T> extends BankBaseMessage {

    private String queryDate;

    private QueryDownloadLogFilesType type;

    private boolean isLast;

    private List<T> data;

    public BankQueryDownloadFilesMessage(String queryDate, QueryDownloadLogFilesType type, List<T> data) {
        super(true, null);
        this.queryDate = queryDate;
        this.type = type;
        this.data = data;
    }

    public String getQueryDate() {
        return queryDate;
    }

    public void setQueryDate(String queryDate) {
        this.queryDate = queryDate;
    }

    public QueryDownloadLogFilesType getType() {
        return type;
    }

    public void setType(QueryDownloadLogFilesType type) {
        this.type = type;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }
}
