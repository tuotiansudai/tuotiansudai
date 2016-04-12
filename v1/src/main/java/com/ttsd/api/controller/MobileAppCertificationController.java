package com.ttsd.api.controller;

import com.esoft.archer.user.exception.UserNotFoundException;
import com.esoft.core.annotations.Logger;
import com.esoft.umpay.trusteeship.exception.UmPayOperationException;
import com.ttsd.api.dto.*;
import com.ttsd.api.service.MobileAppCertificationService;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by tuotian on 15/7/27.
 */
@Controller
public class MobileAppCertificationController {

    @Logger
    Log log;

    @Resource(name = "MobileAppCertificationServiceImpl")
    private MobileAppCertificationService mobileAppCertificationService;

    @RequestMapping(value = "/certificate",method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto userMobileCertification(@RequestBody CertificationRequestDto certificationRequestDto){
        String userId = certificationRequestDto.getBaseParam().getUserId();
        String userRealName = certificationRequestDto.getUserRealName();
        String idCardNumber = certificationRequestDto.getUserIdCardNumber();
        try {
            return mobileAppCertificationService.validateUserCertificationInfo(certificationRequestDto);
        } catch (IOException e) {
            log.error("由于网络异常，ID为：" + userId + " 的用户实名认证失败！");
            log.error(e.getLocalizedMessage(),e);
            return mobileAppCertificationService.assembleResult(ReturnMessage.CERTIFICATION_FAIL.getCode(), ReturnMessage.CERTIFICATION_FAIL.getMsg(), userRealName, idCardNumber);
        } catch (UserNotFoundException e) {
            log.error("获取用户ID为："+userId+" 的用户信息异常！");
            log.error(e.getLocalizedMessage(),e);
            return mobileAppCertificationService.assembleResult(ReturnMessage.USER_ID_NOT_EXIST.getCode(), ReturnMessage.USER_ID_NOT_EXIST.getMsg(), userRealName, idCardNumber);
        } catch (UmPayOperationException e){
            log.error("用户ID为：" + userId + " 的用户使用真实姓名为：" + userRealName + "，身份证号为：" + idCardNumber + "进行实名认证未通过！");
            log.error(e.getLocalizedMessage(),e);
            return mobileAppCertificationService.assembleResult(ReturnMessage.CERTIFICATION_FAIL.getCode(), ReturnMessage.CERTIFICATION_FAIL.getMsg(), userRealName, idCardNumber);
        }
    }

}
