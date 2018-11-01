package com.tuotiansudai.console.controller;

import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tuotiansudai.client.OssApkWrapperClient;
import com.tuotiansudai.console.dto.AppVersionValueDto;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BaseWrapperDataDto;
import com.tuotiansudai.util.HttpClientUtil;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

@RequestMapping("/app")
@Controller
public class AppVersionController {

    static Logger logger = Logger.getLogger(AppVersionController.class);

    @Autowired
    private OssApkWrapperClient ossApkWrapperClient;

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Value("${app:version:info}")
    private String APP_VERSION_INFO_REDIS_KEY;

    @Value("${app.version.check.url}")
    private String APP_VERSION_CHECK_URL;

    @RequestMapping(value = "/version-update", method = RequestMethod.GET)
    public ModelAndView accountBalance() {
        return new ModelAndView("upload_apk");
    }

    @RequestMapping(value = "/version-update", method = RequestMethod.POST)
    @ResponseBody
    public BaseDataDto upload(MultipartHttpServletRequest request) {
        MultipartFile apkFile = request.getFile("apkFile");
        MultipartFile versionFile = request.getFile("versionFile");
        try {
            ossApkWrapperClient.upload(apkFile.getOriginalFilename(), apkFile.getInputStream());
            ossApkWrapperClient.upload(versionFile.getOriginalFilename(), versionFile.getInputStream());

        } catch (Exception e) {
            logger.error("[upload-apk-version:fail]", e);
            return new BaseWrapperDataDto(false, "上传apk和version.json失败", null);
        }
        return new BaseWrapperDataDto();
    }

    @RequestMapping(value = "/upload/version-json", method = RequestMethod.POST)
    @ResponseBody
    public BaseDataDto uploadVersionJson(MultipartHttpServletRequest request) {
        MultipartFile versionFile = request.getFile("versionFile");
        try {
            String errorMessage = checkVersion(inputStreamToString(versionFile.getInputStream()));
            if (!Strings.isNullOrEmpty(errorMessage)){
                return new BaseDataDto(false, errorMessage);
            }
            ossApkWrapperClient.upload("tuotiansudai_version.json", versionFile.getInputStream());
            redisWrapperClient.del(APP_VERSION_INFO_REDIS_KEY);
        } catch (Exception e) {
            logger.error("[upload version json]", e);
            return new BaseDataDto(false, "上传version.json失败");
        }
        return new BaseDataDto(true);
    }

    @RequestMapping(value = "/look/version-json", method = RequestMethod.GET)
    @ResponseBody
    public BaseDataDto lookVersionJson() {
        String versionValueString = redisWrapperClient.get(APP_VERSION_INFO_REDIS_KEY);
        if (Strings.isNullOrEmpty(versionValueString)){
            versionValueString = HttpClientUtil.getResponseBodyAsString(APP_VERSION_CHECK_URL, "UTF-8");
        }
        try {
            JsonObject versionJson = (JsonObject) new JsonParser().parse(versionValueString);
            AppVersionValueDto appVersionValueDto = getVersionValue(versionJson);
            return new BaseDataDto(true, appVersionValueDto.toString());
        }catch (Exception e){
            logger.error(MessageFormat.format("console look version json fail:{0}", e.getMessage()));
        }
        return new BaseDataDto(false);
    }

    private String inputStreamToString(InputStream is) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i=-1;
        while((i=is.read())!=-1){
            byteArrayOutputStream.write(i);
        }
        return byteArrayOutputStream.toString("utf-8");
    }

    private String checkVersion(String uploadVersionString){
        try{
            JsonObject newVersionJson = (JsonObject) new JsonParser().parse(uploadVersionString);
            JsonObject oldVersionJson = (JsonObject) new JsonParser().parse(HttpClientUtil.getResponseBodyAsString(APP_VERSION_CHECK_URL, "UTF-8"));

            AppVersionValueDto newVersionValue = getVersionValue(newVersionJson);
            AppVersionValueDto oldVersionValue = getVersionValue(oldVersionJson);

            if (oldVersionValue.getAndroidVersion() >= newVersionValue.getAndroidVersion()
                    || oldVersionValue.getAndroidVersionCode() >= newVersionValue.getAndroidVersionCode()
                    || !oldVersionValue.getAndroidUrl().equals(newVersionValue.getAndroidUrl())){
                return MessageFormat.format("上传version.json参数值有误,当前版本参数:{0}", oldVersionValue.toString());
            }

        }catch (Exception e){
            logger.error("[console look version json fail]", e);
            return "上传version.json失败";
        }
        return null;
    }

    private AppVersionValueDto getVersionValue(JsonObject versionJson) {
        return new AppVersionValueDto(
                versionJson.get("android").getAsJsonObject().get("version").getAsString(),
                versionJson.get("android").getAsJsonObject().get("versionCode").getAsString(),
                versionJson.get("android").getAsJsonObject().get("url").getAsString(),
                versionJson.get("android").getAsJsonObject().get("forceUpgrade").getAsString(),
                versionJson.get("android").getAsJsonObject().get("message").getAsString(),
                versionJson.get("ios").getAsJsonObject().get("version").getAsString(),
                versionJson.get("ios").getAsJsonObject().get("versionCode").getAsString(),
                versionJson.get("ios").getAsJsonObject().get("url").getAsString(),
                versionJson.get("ios").getAsJsonObject().get("forceUpgrade").getAsString(),
                versionJson.get("ios").getAsJsonObject().get("message").getAsString());
    }
}
