package com.ttsd.api.service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by tuotian on 15/7/27.
 */
public interface MobileAppCertificationService {
    /**
     * @function 平台登录用户实名认证接口
     * @param userId 登录用户ID
     * @param userRealName 登录用户真实姓名
     * @param idCardNumber 身份证号码
     * @return String
     */
    Map<String,Object> validateUserCertificationInfo(String userId,String userRealName,String idCardNumber);

    /**
     * @function 获取用户实名认证信息
     * @param userId 用户ID
     * @return String
     */
    Map<String,Object> getUserCertificationInfo(String userId);
}
