package com.tuotiansudai.api.dto;

import java.io.Serializable;

public class HTrackingRequestInfoDto implements Serializable {

    private String uid;

    private String idfa;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIdfa() {
        return idfa;
    }

    public void setIdfa(String idfa) {
        this.idfa = idfa;
    }
}
