package com.tuotiansudai.etcd;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class ETCDEndPoints {

    private ETCDEndPoint dev;
    private ETCDEndPoint qa1;
    private ETCDEndPoint qa2;
    private ETCDEndPoint qa3;
    private ETCDEndPoint qa4;
    private ETCDEndPoint qa5;

    public ETCDEndPoint getDev() {
        return dev;
    }

    public void setDev(ETCDEndPoint dev) {
        this.dev = dev;
    }

    public ETCDEndPoint getQa1() {
        return qa1;
    }

    public void setQa1(ETCDEndPoint qa1) {
        this.qa1 = qa1;
    }

    public ETCDEndPoint getQa2() {
        return qa2;
    }

    public void setQa2(ETCDEndPoint qa2) {
        this.qa2 = qa2;
    }

    public ETCDEndPoint getQa3() {
        return qa3;
    }

    public void setQa3(ETCDEndPoint qa3) {
        this.qa3 = qa3;
    }

    public ETCDEndPoint getQa4() {
        return qa4;
    }

    public void setQa4(ETCDEndPoint qa4) {
        this.qa4 = qa4;
    }

    public ETCDEndPoint getQa5() {
        return qa5;
    }

    public void setQa5(ETCDEndPoint qa5) {
        this.qa5 = qa5;
    }

    ETCDEndPoint getEndpoint(String env) {
        return Maps.newHashMap(ImmutableMap.<String, ETCDEndPoint>builder()
                .put("dev", dev)
                .put("qa1", qa1)
                .put("qa2", qa2)
                .put("qa3", qa3)
                .put("qa4", qa4)
                .put("qa5", qa5)
                .build()).get(env);
    }
}
