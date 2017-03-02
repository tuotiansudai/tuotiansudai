package com.tuotiansudai.web.config.freemarker;

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

public class StaticResourceDto implements Serializable {
    private Map<String, String> jsFile = Maps.newHashMap();

    private Map<String, String> cssFile = Maps.newHashMap();

    Map<String, String> getJsFile() {
        return jsFile;
    }

    public void setJsFile(Map<String, String> jsFile) {
        this.jsFile = jsFile;
    }

    Map<String, String> getCssFile() {
        return cssFile;
    }

    public void setCssFile(Map<String, String> cssFile) {
        this.cssFile = cssFile;
    }
}
