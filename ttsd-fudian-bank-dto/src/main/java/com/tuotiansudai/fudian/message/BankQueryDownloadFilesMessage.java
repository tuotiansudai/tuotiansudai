package com.tuotiansudai.fudian.message;


import com.tuotiansudai.fudian.download.QueryDownloadLogFilesType;

import java.util.List;

public class BankQueryDownloadFilesMessage<T> extends BankBaseMessage {

    private String queryDate;

    private QueryDownloadLogFilesType type;

    private int total;

    private List<T> data;

    public BankQueryDownloadFilesMessage(String queryDate, QueryDownloadLogFilesType type, int total, List<T> data) {
        super(true, null);
        this.queryDate = queryDate;
        this.type = type;
        this.total = total;
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
