package com.tuotiansudai.api.controller;

import com.google.gson.JsonObject;
import com.tuotiansudai.api.dto.AppVersionResponseDataDto;
import com.tuotiansudai.api.dto.BaseParamDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.util.GsonUtil;
import com.tuotiansudai.api.util.HttpClientUtil;
import com.tuotiansudai.client.RedisWrapperClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MobileAppCheckVersionController {

    private static final String APP_VERSION_CHECK_URL = "https://tuotiansudai.com/app/version.json";
    private static final String APP_VERSION_INFO_REDIS_KEY = "app:version:info";
    private static final int APP_VERSION_INFO_EXPIRE_SECONDS = 60*60;
    static Logger log = Logger.getLogger(MobileAppCheckVersionController.class);

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @RequestMapping(value = "/get/version", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto getAndroidVersion(@RequestBody BaseParamDto dto) {
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
            String jsonString = redisWrapperClient.get(APP_VERSION_INFO_REDIS_KEY);
            if(StringUtils.isBlank(jsonString)) {
                jsonString = HttpClientUtil.getResponseBodyAsString(APP_VERSION_CHECK_URL, "UTF-8");
                redisWrapperClient.setex(APP_VERSION_INFO_REDIS_KEY, APP_VERSION_INFO_EXPIRE_SECONDS,jsonString);
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
