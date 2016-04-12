package com.ttsd.api.controller;

import com.esoft.core.annotations.Logger;
import com.esoft.core.util.GsonUtil;
import com.esoft.core.util.HttpClientUtil;
import com.google.gson.JsonObject;
import com.ttsd.api.dto.AppVersionResponseDataDto;
import com.ttsd.api.dto.BaseParamDto;
import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.ReturnMessage;
import com.ttsd.redis.RedisClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;

@Controller
public class MobileAppCheckVersionController {

    private static final String APP_VERSION_CHECK_URL = "https://tuotiansudai.com/app/version.json";
    private static final String APP_VERSION_INFO_REDIS_KEY = "app:version:info";
    private static final int APP_VERSION_INFO_EXPIRE_SECONDS = 60*60;

    @Logger
    Log log;

    @Autowired
    RedisClient redisClient;

    @Autowired
    ServletContext servletContext;

    @RequestMapping(value = "/get/version", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto getVersion(@RequestBody BaseParamDto dto) {
        BaseResponseDto<AppVersionResponseDataDto> baseResponseDto = new BaseResponseDto<>();
        AppVersionResponseDataDto dataDto = getLastestVersionInfo();
        if (dataDto != null) {
            baseResponseDto.setData(dataDto);
            baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
            baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        } else {
            baseResponseDto.setData(new AppVersionResponseDataDto());
            baseResponseDto.setCode(ReturnMessage.CANNOT_GET_APK_VERSION.getCode());
            baseResponseDto.setMessage(ReturnMessage.CANNOT_GET_APK_VERSION.getMsg());
        }
        return baseResponseDto;
    }

    private AppVersionResponseDataDto getLastestVersionInfo() {
        try {
            String jsonString = redisClient.get(APP_VERSION_INFO_REDIS_KEY);
            if(StringUtils.isBlank(jsonString)) {
                jsonString = HttpClientUtil.getResponseBodyAsString(APP_VERSION_CHECK_URL, "UTF-8");
                redisClient.setex(APP_VERSION_INFO_REDIS_KEY, jsonString, APP_VERSION_INFO_EXPIRE_SECONDS);
            }
            JsonObject json = GsonUtil.stringToJsonObject(jsonString);
            AppVersionResponseDataDto dto = new AppVersionResponseDataDto();
            dto.setForceUpgrade(json.get("forceUpgrade").getAsBoolean());
            dto.setMessage(json.get("message").getAsString());
            dto.setUrl(json.get("url").getAsString());
            dto.setVersion(json.get("version").getAsString());
            dto.setVersionCode(json.get("versionCode").getAsInt());
            return dto;
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }
}
