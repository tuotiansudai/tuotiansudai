package com.tuotiansudai.etcd;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ETCDEndPoints {

    private List<String> dev;
    private List<String> qa1;
    private List<String> qa2;
    private List<String> qa3;
    private List<String> qa4;
    private List<String> qa5;
    private List<String> ut;
    private List<String> prod;
    private List<String> ft;
    private List<String> hzft;

    public List<String> getDev() {
        return dev;
    }

    public void setDev(List<String> dev) {
        this.dev = dev;
    }

    public List<String> getQa1() {
        return qa1;
    }

    public void setQa1(List<String> qa1) {
        this.qa1 = qa1;
    }

    public List<String> getQa2() {
        return qa2;
    }

    public void setQa2(List<String> qa2) {
        this.qa2 = qa2;
    }

    public List<String> getQa3() {
        return qa3;
    }

    public void setQa3(List<String> qa3) {
        this.qa3 = qa3;
    }

    public List<String> getQa4() {
        return qa4;
    }

    public void setQa4(List<String> qa4) {
        this.qa4 = qa4;
    }

    public List<String> getQa5() {
        return qa5;
    }

    public void setQa5(List<String> qa5) {
        this.qa5 = qa5;
    }

    public List<String> getUt() {
        return ut;
    }

    public void setUt(List<String> ut) {
        this.ut = ut;
    }

    public List<String> getProd() {
        return prod;
    }

    public void setProd(List<String> prod) {
        this.prod = prod;
    }

    public List<String> getFt() {
        return ft;
    }

    public void setFt(List<String> ft) {
        this.ft = ft;
    }

    public List<String> getHzft() {
        return hzft;
    }

    public void setHzft(List<String> hzft) {
        this.hzft = hzft;
    }

    List<String> getEndpoint(String env) {
        env = Strings.isNullOrEmpty(env) ? null : env.toLowerCase();
        Map<String, List<String>> endpointsMapping = Maps.newHashMap(ImmutableMap.<String, List<String>>builder()
                .put("dev", dev)
                .put("ut", ut)
                .put("prod", prod)
                .put("qa1", qa1)
                .put("qa2", qa2)
                .put("qa3", qa3)
                .put("qa4", qa4)
                .put("qa5", qa5)
                .put("ft", ft)
                .put("hzft", ft)
                .build());
        List<String> endpoints = endpointsMapping.containsKey(env) ? endpointsMapping.get(env) : endpointsMapping.get("dev");


        return endpoints.stream().map(endpoint -> MessageFormat.format("http://{0}", endpoint)).collect(Collectors.toList());
    }
}
