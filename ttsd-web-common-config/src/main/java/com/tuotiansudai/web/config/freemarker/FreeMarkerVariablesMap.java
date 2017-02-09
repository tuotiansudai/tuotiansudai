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
import java.text.MessageFormat;
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

        StaticResourceDto staticResourceDto = this.discoverStaticResource(map.get("commonStaticServer"));

        Map<String, String> javascriptResource = staticResourceDto.getJsFile();
        if (!Strings.isNullOrEmpty(javascriptLocation)) {
            javascriptResource.putAll(buildStaticFiles(map.get("staticServer").toString(), javascriptLocation, ".js"));
        }

        Map<String, String> cssResource = staticResourceDto.getCssFile();
        if (!Strings.isNullOrEmpty(cssLocation)) {
            cssResource.putAll(buildStaticFiles(map.get("staticServer").toString(), cssLocation, ".css"));
        }

        map.put("js", javascriptResource);
        map.put("css", cssResource);

        return map;
    }

    private Map<String, String> buildStaticFiles(String staticServer, String filePath, final String extension) {
        FilenameFilter filenameFilter = (dir, name) -> name.toLowerCase().endsWith(extension);

        try {
            Resource staticResource = resourceLoader.getResource(filePath);
            File staticFolder = staticResource.getFile();
            if (staticFolder.isDirectory()) {
                File[] staticFiles = staticFolder.listFiles(filenameFilter);
                return this.generateVersionMap(staticServer, filePath, staticFiles);
            }
        } catch (IOException e) {
            logger.error("Generate Static Resource Version Map Failed: ", e);
        }
        return Collections.emptyMap();
    }

    private Map<String, String> generateVersionMap(String staticServer, String filePath, File[] staticFiles) {
        Map<String, String> versionMap = Maps.newHashMap();

        for (File file : staticFiles) {
            String fileName = file.getName();
            String[] split = fileName.split("\\.");
            String filePrefix = split[0];

            if (!versionMap.containsKey(filePrefix)) {
                versionMap.put(filePrefix, MessageFormat.format("{0}{1}{2}", staticServer, filePath,fileName));
            }

            if (split.length == PROD_VERSION_LENGTH) {
                versionMap.put(filePrefix, fileName);
            }

        }
        return versionMap;
    }

    private StaticResourceDto discoverStaticResource(Object commonStaticServer) {
        if (commonStaticServer == null || Strings.isNullOrEmpty(this.staticResourceDiscoveryUrl)) {
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
                throw new RuntimeException(MessageFormat.format("static server response is {0}, request url {1}", response.code(), this.staticResourceDiscoveryUrl));
            }

            String responseBody = response.body().string();
            logger.info(MessageFormat.format("static server response is {0}, request url {1}, response {2}",
                    response.code(),
                    this.staticResourceDiscoveryUrl,
                    responseBody));
            StaticResourceDto staticResourceDto = JsonConverter.readValue(responseBody, StaticResourceDto.class);
            for (String key : staticResourceDto.getJsFile().keySet()) {
                staticResourceDto.getJsFile().put(key, MessageFormat.format("{0}{1}", commonStaticServer.toString(), staticResourceDto.getJsFile().get(key)));
            }
            for (String key : staticResourceDto.getCssFile().keySet()) {
                staticResourceDto.getCssFile().put(key, MessageFormat.format("{0}{1}", commonStaticServer.toString(), staticResourceDto.getCssFile().get(key)));
            }
            return staticResourceDto;
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
