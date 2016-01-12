package com.tuotiansudai.api.controller;

import com.google.gson.JsonObject;
import com.tuotiansudai.api.dto.AppVersionResponseDataDto;
import com.tuotiansudai.api.dto.BaseParamDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.util.GsonUtil;
import com.tuotiansudai.api.util.HttpClientUtil;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.repository.model.Source;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MobileAppCheckVersionController extends MobileAppBaseController {

    private static final String APP_ANDROID_VERSION_CHECK_URL = "https://tuotiansudai.com/app/android_version.json";
    private static final String APP_IOS_VERSION_CHECK_URL = "https://tuotiansudai.com/app/ios_version.json";
    private static final String APP_ANDROID_VERSION_INFO_REDIS_KEY = "app:android_version:info";
    private static final String APP_IOS_VERSION_INFO_REDIS_KEY = "app:ios_version:info";
    private static final int APP_VERSION_INFO_EXPIRE_SECONDS = 60 * 60;
    static Logger log = Logger.getLogger(MobileAppCheckVersionController.class);

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @RequestMapping(value = "/get/version", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto getAndroidVersion(@RequestBody BaseParamDto dto) {
        BaseResponseDto<AppVersionResponseDataDto> baseResponseDto = new BaseResponseDto<>();
        AppVersionResponseDataDto dataDto = getLastestVersionInfo(dto.getBaseParam().getPlatform());
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

    private AppVersionResponseDataDto getLastestVersionInfo(String platform) {
        try {
            String redisKey = "";
            String url = "";
            if (platform.equalsIgnoreCase(Source.ANDROID.name())) {
                redisKey = APP_ANDROID_VERSION_INFO_REDIS_KEY;
                url = APP_ANDROID_VERSION_CHECK_URL;
            } else if (platform.equalsIgnoreCase(Source.IOS.name())) {
                redisKey = APP_IOS_VERSION_INFO_REDIS_KEY;
                url = APP_IOS_VERSION_CHECK_URL;
            }
            String jsonString = redisWrapperClient.get(redisKey);
            if (StringUtils.isBlank(jsonString)) {
                jsonString = HttpClientUtil.getResponseBodyAsString(url, "UTF-8");
                redisWrapperClient.setex(redisKey, APP_VERSION_INFO_EXPIRE_SECONDS, jsonString);
            }
            JsonObject json = GsonUtil.stringToJsonObject(jsonString);
            AppVersionResponseDataDto dto = new AppVersionResponseDataDto();
            dto.setForceUpgrade(json.get("forceUpgrade").getAsBoolean());
            dto.setMessage(json.get("message").getAsString());
            dto.setVersion(json.get("version").getAsString());
            if (platform.equalsIgnoreCase(Source.ANDROID.name())){
                dto.setVersionCode(json.get("versionCode").getAsInt());
                dto.setUrl(json.get("url").getAsString());
            }
            return dto;
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }
}
