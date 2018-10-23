package com.tuotiansudai.repository.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.HashMap;

public enum TransferStatus {
    TRANSFERABLE("申请转让"),
    OVERDUE_TRANSFERABLE("逾期申请转让"),
    OVERDUE_TRANSFERRING("逾期转让中"),
    OVERDUE_SUCCESS("逾期已转让"),
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

    public static TransferStatus getTransferStatus(TransferStatus transferStatus){
        HashMap transferStatusMap =  Maps.newHashMap(ImmutableMap.<TransferStatus, TransferStatus>builder()
                .put(TransferStatus.OVERDUE_TRANSFERABLE, TransferStatus.TRANSFERABLE)
                .put(TransferStatus.OVERDUE_TRANSFERRING, TransferStatus.TRANSFERABLE)
                .put(TransferStatus.OVERDUE_SUCCESS, TransferStatus.SUCCESS)
                .build());

        if (transferStatusMap.containsKey(transferStatus)){
            return (TransferStatus) transferStatusMap.get(transferStatus);
        }
        return transferStatus;
    }

}
