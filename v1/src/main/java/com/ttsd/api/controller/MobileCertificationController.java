package com.ttsd.api.controller;

import com.ttsd.api.service.MobileAppCertificationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by tuotian on 15/7/27.
 */
@Controller
@RequestMapping(value = "/mobileAppCertification")
public class MobileCertificationController {

    @Resource(name = "MobileAppCertificationServiceImpl")
    MobileAppCertificationService mobileAppCertificationService;

    @RequestMapping(value = "/certification",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> userMobileCertification(@RequestParam(value = "userId") String userId,
                                          @RequestParam(value = "userRealName") String userRealName,
                                          @RequestParam(value = "userIdCardNumber") String userIdCardNumber){
        return mobileAppCertificationService.validateUserCertificationInfo(userId,userRealName,userIdCardNumber);
    }

    @RequestMapping(value = "/getUserCertificationInfo",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getUserCertificationInfo(@RequestParam(value = "userId")String userId){
        return mobileAppCertificationService.getUserCertificationInfo(userId);
    }

    public void setMobileAppCertificationService(MobileAppCertificationService mobileAppCertificationService) {
        this.mobileAppCertificationService = mobileAppCertificationService;
    }
}
