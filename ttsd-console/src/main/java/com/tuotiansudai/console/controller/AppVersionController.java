package com.tuotiansudai.console.controller;

import com.tuotiansudai.client.OssApkWrapperClient;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseWrapperDataDto;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by qduljs2011 on 2018/10/29.
 */
@RequestMapping("/app")
@Controller
public class AppVersionController {
    static Logger logger = Logger.getLogger(AppVersionController.class);
    @Autowired
    private OssApkWrapperClient ossApkWrapperClient;
    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @RequestMapping(value = "/version-update", method = RequestMethod.GET)
    public ModelAndView accountBalance() {
        return new ModelAndView("upload_apk");
    }

    @RequestMapping(value = "/version-update", method = RequestMethod.POST)
    @ResponseBody
    public BaseDataDto upload(MultipartHttpServletRequest request) {
        MultipartFile apkFile = request.getFile("apkFile");
        MultipartFile versionFile = request.getFile("versionFile");
        try{
            ossApkWrapperClient.upload(apkFile.getOriginalFilename(),apkFile.getInputStream());
            ossApkWrapperClient.upload(versionFile.getOriginalFilename(),versionFile.getInputStream());

        }catch(Exception e){
             logger.error("[upload-apk-version:fail]",e);
             return new BaseWrapperDataDto(false,"上传apk和version.json失败",null);
        }
        return new BaseWrapperDataDto();
    }
}
