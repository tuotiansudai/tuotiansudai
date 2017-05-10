package com.tuotiansudai.console.activity.config;

import com.fasterxml.jackson.core.JsonParser;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.web.config.handler.HandlerExceptionLoggingResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

    @Value("${web.server}")
    private String webServer;

    @Value("${common.static.server}")
    private String commonStaticServer;

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    // Override configuration methods...
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder()
                .timeZone(GregorianCalendar.getInstance().getTimeZone())
                .dateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                .featuresToEnable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/activity-console/style/**").addResourceLocations("classpath:/static/style/");
        registry.addResourceHandler("/activity-console/images/**").addResourceLocations("classpath:/static/images/");
        registry.addResourceHandler("/activity-console/js/**").addResourceLocations("classpath:/static/js/");
    }

    @Bean
    public FreeMarkerViewResolver freeMarkerViewResolver() {
        FreeMarkerViewResolver freeMarkerViewResolver = new FreeMarkerViewResolver();
        freeMarkerViewResolver.setCache(true);
        freeMarkerViewResolver.setPrefix("");
        freeMarkerViewResolver.setSuffix(".ftl");
        freeMarkerViewResolver.setContentType("text/html; charset=UTF-8");
        freeMarkerViewResolver.setRequestContextAttribute("requestContext");
        freeMarkerViewResolver.setExposeSessionAttributes(true);
        return freeMarkerViewResolver;
    }

    @Bean
    public FreeMarkerConfigurer freemarkerConfig() {
        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
        configurer.setDefaultEncoding("UTF-8");
        configurer.setTemplateLoaderPath("classpath:/templates/");
        configurer.setFreemarkerVariables(Maps.newHashMap(new ImmutableMap.Builder<String, Object>()
                .put("webServer", webServer)
                .put("commonStaticServer", commonStaticServer)
                .build()));
        Properties settings = new Properties();
        settings.setProperty("template_exception_handler", "RETHROW");
        configurer.setFreemarkerSettings(settings);
        return configurer;
    }

    @Bean
    public HandlerExceptionLoggingResolver handlerExceptionLoggingResolver() {
        return new HandlerExceptionLoggingResolver();
    }
}
