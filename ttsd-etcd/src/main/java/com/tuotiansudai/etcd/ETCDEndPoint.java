package com.tuotiansudai.etcd;

import java.util.List;

public class ETCDEndPoint {

    private List<String> host;

    private List<String> port;

    public List<String> getHost() {
        return host;
    }

    public void setHost(List<String> host) {
        this.host = host;
    }

    public List<String> getPort() {
        return port;
    }

    public void setPort(List<String> port) {
        this.port = port;
    }
}
