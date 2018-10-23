package com.tuotiansudai.repository.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.HashMap;

public enum TransferStatus {
    TRANSFERABLE("申请转让"),
    TRANSFERRING("转让中"),
    SUCCESS("已转让"),
    CANCEL("取消转让"),
    NONTRANSFERABLE("不可转让");

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    TransferStatus(String description) {
        this.description = description;
    }
}
