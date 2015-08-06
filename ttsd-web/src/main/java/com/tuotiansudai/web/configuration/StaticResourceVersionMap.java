package com.tuotiansudai.web.configuration;

import com.google.common.collect.Maps;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.config.MapFactoryBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class StaticResourceVersionMap extends MapFactoryBean implements ResourceLoaderAware {

    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    private String javascriptLocation;
    private String cssLocation;

    private static int devVersionLength = 3;
    private static int prodVersionLength = 4;

    @Override
    protected Map<Object, Object> createInstance() {
        Map map = Maps.newHashMap();
        try {
            Resource javascriptResource = resourceLoader.getResource(javascriptLocation);
            File jsFolder = javascriptResource.getFile();
            if (jsFolder.isDirectory()) {
                File[] jsFiles = jsFolder.listFiles();
                map.put("js", this.generateVersionMap(jsFiles));
            }

            Resource cssResource = resourceLoader.getResource(cssLocation);
            File cssFolder = cssResource.getFile();
            if (cssFolder.isDirectory()) {
                File[] cssFiles = cssFolder.listFiles();
                map.put("css", this.generateVersionMap(cssFiles));
            }
        }
        catch (IOException ex) {
            logger.error("Generate Static Resource Version Map Failed: ", ex);
        }
        return map;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    private Map<String, String> generateVersionMap(File[] jsFiles) {
        Map<String, String> versionMap = Maps.newHashMap();
        for (File file : jsFiles) {
            String fileName = file.getName();
            String[] split = fileName.split("\\.");
            String filePrefix = split[0];
            if (split.length == devVersionLength && !versionMap.containsKey(filePrefix)) {
                versionMap.put(filePrefix, fileName);
            }
            if (split.length == prodVersionLength) {
                versionMap.put(filePrefix, fileName);
            }

        }
        return versionMap;
    }

    public void setJavascriptLocation(String javascriptLocation) {
        this.javascriptLocation = javascriptLocation;
    }

    public void setCssLocation(String cssLocation) {
        this.cssLocation = cssLocation;
    }
}
