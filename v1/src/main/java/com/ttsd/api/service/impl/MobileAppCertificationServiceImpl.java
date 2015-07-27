package com.ttsd.api.service.impl;

import com.esoft.archer.user.exception.UserNotFoundException;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.UserService;
import com.esoft.core.annotations.Logger;
import com.esoft.umpay.trusteeship.exception.UmPayOperationException;
import com.esoft.umpay.user.service.impl.UmPayUserOperation;
import com.google.common.base.Strings;
import com.ttsd.api.dto.ReturnMessage;
import com.ttsd.api.service.MobileAppCertificationService;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tuotian on 15/7/27.
 */
@Service(value = "MobileAppCertificationServiceImpl")
public class MobileAppCertificationServiceImpl implements MobileAppCertificationService{

    @Logger
    private Log log;

    @Autowired
    private UmPayUserOperation umPayUserOperation;

    @Autowired
    private UserService userService;

    /**
     * @function 移动端实名认证接口
     * @param userId 登录用户ID
     * @param userRealName 登录用户真实姓名
     * @param idCardNumber 身份证号码
     * @return Map<String,Object>
     */
    @Override
    public Map<String,Object> validateUserCertificationInfo(String userId, String userRealName, String idCardNumber) {
        if (Strings.isNullOrEmpty(userId)){
            return assembleResult(ReturnMessage.USER_ID_IS_NULL.getCode(), ReturnMessage.USER_ID_IS_NULL.getMsg(), userRealName, idCardNumber);
        }
        if (Strings.isNullOrEmpty(userRealName)){
            return assembleResult(ReturnMessage.REAL_NAME_IS_NULL.getCode(), ReturnMessage.REAL_NAME_IS_NULL.getMsg(), userRealName, idCardNumber);
        }
        if (Strings.isNullOrEmpty(idCardNumber)){
            return assembleResult(ReturnMessage.ID_CARD_IS_NULL.getCode(), ReturnMessage.ID_CARD_IS_NULL.getMsg(), userRealName, idCardNumber);
        }
        if (userService.idCardIsExists(idCardNumber)){
            return assembleResult(ReturnMessage.ID_CARD_IS_EXIST.getCode(), ReturnMessage.ID_CARD_IS_EXIST.getMsg(), userRealName, idCardNumber);
        }
        User user = null;
        try {
            user = userService.getUserById(userId);
        } catch (UserNotFoundException e) {
            log.error("获取用户ID为："+userId+" 的用户信息异常！");
            log.error(e.getLocalizedMessage(),e);
            return assembleResult(ReturnMessage.USER_ID_NOT_EXIST.getCode(), ReturnMessage.USER_ID_NOT_EXIST.getMsg(), userRealName, idCardNumber);
        }
        if (user != null){
            user.setRealname(userRealName);
            user.setIdCard(idCardNumber);
            try {
                umPayUserOperation.createOperation(user, null);
                return assembleResult(ReturnMessage.SUCCESS.getCode(), ReturnMessage.SUCCESS.getMsg(), userRealName, idCardNumber);
            } catch (IOException e) {
                log.error("由于网络异常，用户ID为：" + userId + " 的用户实名认证失败！");
                log.error(e.getLocalizedMessage());
                return assembleResult(ReturnMessage.CERTIFICATION_FAIL.getCode(), ReturnMessage.CERTIFICATION_FAIL.getMsg(), userRealName, idCardNumber);
            } catch (UmPayOperationException e){
                log.error("用户ID为："+userId+" 的用户使用真实姓名为："+userRealName+"，身份证号为："+idCardNumber+"进行实名认证未通过！");
                log.error(e.getLocalizedMessage());
                return assembleResult(ReturnMessage.CERTIFICATION_FAIL.getCode(), ReturnMessage.CERTIFICATION_FAIL.getMsg(), userRealName, idCardNumber);
            }
        }
        return null;
    }

    /**
     * @function 移动端获取用户实名认证信息(已经处理过的用户信息)
     * @param userId 用户ID
     * @return Map<String,Object>
     */
    @Override
    public Map<String,Object> getUserCertificationInfo(String userId) {
        if (Strings.isNullOrEmpty(userId)){
            return assembleResult(ReturnMessage.USER_ID_IS_NULL.getCode(), ReturnMessage.USER_ID_IS_NULL.getMsg(), null, null);
        }
        try {
            User user = userService.getUserById(userId);
            String userRealName = user.getRealname();
            String idCardNumber = user.getIdCard();
            userRealName = userRealName.substring(0,1);
            idCardNumber = (idCardNumber.substring(0,6)+"******"+idCardNumber.substring(13,18));
            return assembleResult(ReturnMessage.CERTIFICATION_FAIL.getCode(), ReturnMessage.CERTIFICATION_FAIL.getMsg(), userRealName, idCardNumber);
        } catch (UserNotFoundException e) {
            log.error("由于网络异常，用户ID为：" + userId + " 的用户实名认证认证信息失败！");
            log.error(e.getLocalizedMessage());
            return assembleResult(ReturnMessage.USER_ID_NOT_EXIST.getCode(), ReturnMessage.USER_ID_NOT_EXIST.getMsg(), null, null);
        }
    }

    /**
     * @function 封装结果集
     * @param code 消息代码
     * @param message 消息描述
     * @param userRealName 用户真实姓名
     * @param idCardNumber 用户身份证号码
     * @return Map<String,Object>
     */
    public Map<String,Object> assembleResult(String code,String message,String userRealName,String idCardNumber){
        Map<String,Object> resMap = new HashMap<String,Object>();
        resMap.put("code", code);
        resMap.put("message",message);
        Map<String,String> userInfoMap = new HashMap<String,String>();
        userInfoMap.put("userRealName",userRealName);
        userInfoMap.put("userIdCardNumber",idCardNumber);
        resMap.put("certificationData",userInfoMap);
        return resMap;
    }

    /*************************************set方法**************************************/
    public void setLog(Log log) {
        this.log = log;
    }

    public void setUmPayUserOperation(UmPayUserOperation umPayUserOperation) {
        this.umPayUserOperation = umPayUserOperation;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
