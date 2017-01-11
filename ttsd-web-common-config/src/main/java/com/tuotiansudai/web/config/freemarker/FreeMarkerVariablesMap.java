package com.tuotiansudai.web.config.freemarker;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tuotiansudai.util.JsonConverter;
import org.springframework.beans.factory.config.MapFactoryBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FreeMarkerVariablesMap extends MapFactoryBean implements ResourceLoaderAware {

    private static final int PROD_VERSION_LENGTH = 4;

    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    private OkHttpClient okHttpClient;

    private String javascriptLocation;

    private String cssLocation;

    private String staticResourceDiscoveryUrl;

    public FreeMarkerVariablesMap() {
        this.okHttpClient = new OkHttpClient();
        this.okHttpClient.setConnectTimeout(5, TimeUnit.SECONDS);
    }

    @Override
    protected Map<Object, Object> createInstance() {
        Map<Object, Object> map = super.createInstance();

        map.put("jsPath", javascriptLocation);
        map.put("cssPath", cssLocation);

        StaticResourceDto staticResourceDto = this.discoverStaticResource();

        Map<String, String> javascriptResource = Strings.isNullOrEmpty(javascriptLocation) ? staticResourceDto.getJsFile() : buildStaticFiles(javascriptLocation, ".js");
        Map<String, String> cssResource = Strings.isNullOrEmpty(cssLocation) ? staticResourceDto.getCssFile() : buildStaticFiles(cssLocation, ".css");

        map.put("js", javascriptResource);
        map.put("css", cssResource);

        return map;
    }

    private Map<String, String> buildStaticFiles(String filePath, final String extension) {
        FilenameFilter filenameFilter = (dir, name) -> name.toLowerCase().endsWith(extension);

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

    private StaticResourceDto discoverStaticResource() {
        if (Strings.isNullOrEmpty(this.staticResourceDiscoveryUrl)) {
            logger.info("static resource discovery url is empty, skip discovery");
            return new StaticResourceDto();
        }

        Request request = new Request.Builder()
                .url(this.staticResourceDiscoveryUrl)
                .get()
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (!HttpStatus.valueOf(response.code()).is2xxSuccessful()) {
                throw new RuntimeException("static server response is not 2XX");
            }
            String responseBody = response.body().string();
            return JsonConverter.readValue(responseBody, StaticResourceDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
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

    public void setStaticResourceDiscoveryUrl(String staticResourceDiscoveryUrl) {
        this.staticResourceDiscoveryUrl = staticResourceDiscoveryUrl;
    }
}
