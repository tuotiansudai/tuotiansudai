package com.tuotiansudai.web.config.freemarker;

import com.google.common.base.Strings;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tuotiansudai.util.JsonConverter;
import org.springframework.beans.factory.config.MapFactoryBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FreeMarkerVariablesMap extends MapFactoryBean implements ResourceLoaderAware {

    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    private OkHttpClient okHttpClient;

    private String staticResourceDiscoveryUrl;

    public FreeMarkerVariablesMap() {
        this.okHttpClient = new OkHttpClient();
        this.okHttpClient.setConnectTimeout(5, TimeUnit.SECONDS);
        this.okHttpClient.setReadTimeout(5, TimeUnit.SECONDS);
        this.okHttpClient.setWriteTimeout(5, TimeUnit.SECONDS);
    }

    @Override
    protected Map<Object, Object> createInstance() {
        Map<Object, Object> map = super.createInstance();

        StaticResourceDto staticResourceDto = this.discoverStaticResource(map.get("commonStaticServer"));

        Map<String, String> javascriptResource = staticResourceDto.getJsFile();
        Map<String, String> cssResource = staticResourceDto.getCssFile();

        map.put("js", javascriptResource);
        map.put("css", cssResource);
        logger.info(MessageFormat.format("js mapping: {0}", javascriptResource.toString()));
        logger.info(MessageFormat.format("css mapping: {0}", cssResource.toString()));

        return map;
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

    public void setStaticResourceDiscoveryUrl(String staticResourceDiscoveryUrl) {
        this.staticResourceDiscoveryUrl = staticResourceDiscoveryUrl;
    }
}
