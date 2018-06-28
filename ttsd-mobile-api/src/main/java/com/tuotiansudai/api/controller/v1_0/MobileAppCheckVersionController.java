package com.tuotiansudai.api.controller.v1_0;

import com.google.gson.JsonObject;
import com.tuotiansudai.api.dto.v1_0.AppVersionResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.util.GsonUtil;
import com.tuotiansudai.api.util.HttpClientUtil;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.util.RedisWrapperClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
@Api(description = "Android版本检测")
public class MobileAppCheckVersionController extends MobileAppBaseController {

    private static Logger logger = Logger.getLogger(MobileAppCheckVersionController.class);

    private static final String APP_VERSION_CHECK_URL = "https://tuotiansudai.com/app/version.json";
    private static final String VERSION_CONFIG_FILE = "version.json";
    private static final String APP_VERSION_INFO_REDIS_KEY = "app:version:info";
    private static final int APP_VERSION_INFO_EXPIRE_SECONDS = 60 * 60;
    private static final Logger log = Logger.getLogger(MobileAppCheckVersionController.class);

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Value("${common.environment}")
    private Environment environment;

    @RequestMapping(value = "/get/version", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("获取版本")
    public BaseResponseDto<AppVersionResponseDataDto> getAndroidVersion(@RequestBody BaseParamDto dto) {
        BaseResponseDto<AppVersionResponseDataDto> baseResponseDto = new BaseResponseDto<>();
        AppVersionResponseDataDto dataDto = getLatestVersionInfo(dto.getBaseParam().getPlatform());
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

    private AppVersionResponseDataDto getLatestVersionInfo(String platform) {
        try {
            String jsonString = redisWrapperClient.get(APP_VERSION_INFO_REDIS_KEY);
            if (StringUtils.isBlank(jsonString)) {
                if (Environment.isProduction(environment)) {
                    jsonString = HttpClientUtil.getResponseBodyAsString(APP_VERSION_CHECK_URL, "UTF-8");
                } else {
                    InputStream inputStream = MobileAppCheckVersionController.class.getClassLoader().getResourceAsStream(VERSION_CONFIG_FILE);
                    jsonString = inputStreamToString(inputStream);
                }
                redisWrapperClient.setex(APP_VERSION_INFO_REDIS_KEY, APP_VERSION_INFO_EXPIRE_SECONDS, jsonString);
            }
            JsonObject json = GsonUtil.stringToJsonObject(jsonString);
            logger.info("MobileAppCheckVersionController json = " + json);
            AppVersionResponseDataDto dto = new AppVersionResponseDataDto();
            String jsonKey = platform.toLowerCase();
            logger.info("MobileAppCheckVersionController jsonKey = " + jsonKey);
            logger.info("MobileAppCheckVersionController json.get(jsonKey) = " + json.get(jsonKey).getAsJsonObject());
            dto.setForceUpgrade(json.get(jsonKey).getAsJsonObject().get("forceUpgrade").getAsBoolean());
            dto.setMessage(json.get(jsonKey).getAsJsonObject().get("message").getAsString());
            dto.setVersion(json.get(jsonKey).getAsJsonObject().get("version").getAsString());
            if (platform.equalsIgnoreCase(Source.ANDROID.name())) {
                dto.setVersionCode(json.get(jsonKey).getAsJsonObject().get("versionCode").getAsInt());
                dto.setUrl(json.get(jsonKey).getAsJsonObject().get("url").getAsString());
            }
            return dto;
        } catch (Exception e) {
            log.error("getLatestVersionInfo failed. ", e);
        }
        return null;
    }

    private String inputStreamToString(InputStream is) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            byteArrayOutputStream.write(i);
        }
        return byteArrayOutputStream.toString("utf-8");
    }

}
