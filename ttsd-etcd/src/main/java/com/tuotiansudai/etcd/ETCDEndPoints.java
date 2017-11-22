package com.tuotiansudai.etcd;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class ETCDEndPoints {

    private ETCDEndPoint dev;

    private ETCDEndPoint qa1;

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

    public ETCDEndPoint getEndpoint(String env) {
        return Maps.newHashMap(ImmutableMap.<String, ETCDEndPoint>builder()
                .put("dev", dev)
                .put("qa1", qa1)
                .build()).get(env);
    }
}
