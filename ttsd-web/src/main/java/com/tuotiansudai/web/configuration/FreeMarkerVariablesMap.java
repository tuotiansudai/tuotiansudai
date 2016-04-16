package com.tuotiansudai.web.configuration;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.tuotiansudai.service.LinkExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.MapFactoryBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class FreeMarkerVariablesMap extends MapFactoryBean implements ResourceLoaderAware {

    private static final int PROD_VERSION_LENGTH = 4;
    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    private String staticServer = "";

    private String javascriptLocation;

    private String cssLocation;

    @Autowired
    private LinkExchangeService linkExchangeService;

    @Override
    protected Map<Object, Object> createInstance() {
        Map<Object, Object> map = super.createInstance();

        map.put("staticServer", Strings.isNullOrEmpty(staticServer) ? "" : staticServer);
        map.put("jsPath", javascriptLocation);
        map.put("cssPath", cssLocation);

        map.put("js", buildStaticFiles(javascriptLocation, ".js"));
        map.put("css", buildStaticFiles(cssLocation, ".css"));

        map.put("linkExchangeList", linkExchangeService.getLinkExchangeListByAsc());
        return map;
    }

    private Map<String, String> buildStaticFiles(String filePath, final String extension) {
        FilenameFilter filenameFilter = new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(extension);
            }
        };

        try {
            Resource javascriptResource = resourceLoader.getResource(filePath);
            File jsFolder = javascriptResource.getFile();
            if (jsFolder.isDirectory()) {
                File[] jsFiles = jsFolder.listFiles(filenameFilter);
                return this.generateVersionMap(jsFiles);
            }
        } catch (IOException e) {
            logger.error("Generate Static Resource Version Map Failed: ", e);
        }
        return Collections.emptyMap();
    }

    private Map<String, String> generateVersionMap(File[] jsFiles) {
        Map<String, String> versionMap = Maps.newHashMap();

        for (File file : jsFiles) {
            String fileName = file.getName();
            String[] split = fileName.split("\\.");
            String filePrefix = split[0];

            if (!versionMap.containsKey(filePrefix)) {
                versionMap.put(filePrefix, fileName);
            }

            if (split.length == PROD_VERSION_LENGTH) {
                versionMap.put(filePrefix, fileName);
            }

        }
        return versionMap;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void setJavascriptLocation(String javascriptLocation) {
        this.javascriptLocation = javascriptLocation;
    }

    public void setCssLocation(String cssLocation) {
        this.cssLocation = cssLocation;
    }

    public void setStaticServer(String staticServer) {
        this.staticServer = staticServer;
    }
}
