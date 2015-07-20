package com.ttsd.mobile.service.impl;

import com.esoft.archer.common.CommonConstants;
import com.esoft.archer.common.exception.AuthInfoAlreadyActivedException;
import com.esoft.archer.common.exception.AuthInfoOutOfDateException;
import com.esoft.archer.common.exception.NoMatchingObjectsException;
import com.esoft.archer.common.service.AuthService;
import com.esoft.archer.user.UserConstants;
import com.esoft.archer.user.model.Role;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.UserService;
import com.esoft.archer.user.service.impl.UserBO;
import com.esoft.core.annotations.Logger;
import com.esoft.core.util.DateStyle;
import com.esoft.core.util.DateUtil;
import com.esoft.core.util.HashCrypt;
import com.esoft.jdp2p.message.MessageConstants;
import com.esoft.jdp2p.message.model.UserMessageTemplate;
import com.esoft.jdp2p.message.service.impl.MessageBO;
import com.ttsd.aliyun.PropertiesUtils;
import com.ttsd.mobile.dao.IMobileRegisterDao;
import com.ttsd.mobile.service.IMobileRegisterService;
import com.ttsd.util.CommonUtils;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tuotian on 15/7/9.
 */
@Service(value = "mobileRegisterServiceImpl")
public class MobileRegisterServiceImpl implements IMobileRegisterService {
    @Logger
    private Log log;

    @Resource(name = "mobileRegisterDaoImpl")
    private IMobileRegisterDao mobileRegisterDao;

    @Resource
    private AuthService authService;

    @Resource(name = "messageBO")
    private MessageBO messageBO;

    @Resource(name = "userBO")
    private UserBO userBO;

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public String mobileRegister(String userName, String password, String phoneNum, String vCode,String operationType) {
        if (userName == null || userName.equals("")){
            log.info("提交的用户名为空！");
            return "false";
        }else {
            int count = mobileRegisterDao.getUserCountByUserName(userName);
            log.info("用户名为"+userName+"的用户有"+count+"个！");
            if(count > 0){
                log.info("用户名为"+userName+"的用户已经存在！");
                return "false";
            }
        }

        if (password == null || password.equals("")){
            log.info("提交的密码为空！");
            return "false";
        }

        if (phoneNum == null || phoneNum.equals("")){
            log.info("提交的手机号为空！");
            return "false";
        }else {
            log.info("提交的手机号为"+phoneNum);
            int count = mobileRegisterDao.getUserCountByCellphone(phoneNum);
            if (count > 0){
                log.info("手机号为"+phoneNum+"的用户已经存在！");
                return "false";
            }else {
                if (operationType.equals("0")) {
                    String authCode = null;
                    if (regValidatePhoneNum(phoneNum)) {
                        try {
                            //授权验证码的有效期为自发送短信时起，十分钟内有效，配置在common.properties文件中
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(new Date());
                            calendar.add(Calendar.MINUTE, new Integer(PropertiesUtils.getPro("authMessageValidTime").trim()));
                            Long validTime = calendar.getTimeInMillis();
                            Date deadLine = new Date(validTime);
                            //创建授权码，并持久化到数据库中
                            authCode = authService.createAuthInfo(userName, phoneNum, deadLine, CommonConstants.AuthInfoType.REGISTER_BY_MOBILE_NUMBER).getAuthCode();
                            log.info("已为用户名为："+userName+",手机号为："+phoneNum+"的用户成功创建激活码！");
                        }catch (Exception e){
                            log.error("生成授权码异常！");
                            e.printStackTrace();
                        }
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("time", DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
                        params.put("authCode", authCode);
                        if(!CommonUtils.isDevEnvironment("environment")){
                            try {
                                //给指定用户发送授权码
                                messageBO.sendSMS(mobileRegisterDao.getMessageTemplate(UserMessageTemplate.class,MessageConstants.UserMessageNodeId.REGISTER_BY_MOBILE_NUMBER + "_sms"), params, phoneNum);
                                log.info("给用户名为："+userName+"，手机号为："+phoneNum+"的用户发送授权码成功！");
                            }catch (Exception e){
                                log.error("持久化手机号为"+phoneNum+"的用户的注册授权码失败！");
                                e.printStackTrace();
                            }
                        }
                        return "ture";
                    }
                }else if (operationType.equals("1")){
                    int codeCount = 0;
                    try {
                        codeCount = mobileRegisterDao.getAuthInfo(phoneNum,vCode,CommonConstants.AuthInfoStatus.INACTIVE);
                    }catch (Exception e){
                        log.error("获取用户名为："+userName+",手机号为："+phoneNum+"的用户授权码信息失败！");
                    }
                    if(codeCount > 0) {
                        User user = new User();
                        user.setRegisterTime(new Date());
                        user.setUsername(userName);
                        user.setMobileNumber(phoneNum);
                        // 用户密码通过sha加密
                        user.setPassword(HashCrypt.getDigestHash(password));
                        user.setStatus(UserConstants.UserStatus.ENABLE);
                        try {
                            userBO.save(user);
                            log.info("用户名为："+userName+",手机号为："+phoneNum+"的用户信息持久化成功！");
                        }catch (Exception e1){
                            log.error("用户名为："+userName+",手机号为："+phoneNum+"的用户信息持久化失败！");
                            e1.printStackTrace();
                        }
                        // 添加普通用户权限
                        Role role = new Role("MEMBER");
                        try {
                            userBO.addRole(user, role);
                            log.info("给用户名为：" + userName + ",手机号为：" + phoneNum + "的用户添加普通用户权限成功！");
                            try {
                                //用户注册成功后，将发给该用户的注册码状态更新成已激活状态
                                mobileRegisterDao.updateUserAuthInfo(CommonConstants.AuthInfoStatus.ACTIVATED, phoneNum,CommonConstants.AuthInfoType.REGISTER_BY_MOBILE_NUMBER);
                            }catch (Exception e){
                                log.error("更新用户名为："+userName+",手机号为："+phoneNum+"的授权码状态失败！");
                                e.printStackTrace();
                            }
                        }catch (Exception e){
                            log.error("给用户名为："+userName+",手机号为："+phoneNum+"的用户添加普通用户权限失败！");
                            e.printStackTrace();
                        }
                        return "true";
                    }else {
                        return "false";
                    }
                }else {
                    return "false";
                }
            }
        }
        return null;
    }

    @Override
    public String validateUserName(String userName) {
        int count = mobileRegisterDao.getUserCountByUserName(userName);
        if (count > 0){
            return "false";
        }
        return "true";
    }

    @Override
    public String validateMobilePhoneNum(String phoneNumber) {
        if (regValidatePhoneNum(phoneNumber)){
            int count = mobileRegisterDao.getUserCountByCellphone(phoneNumber);
            if (count > 0){
                return "false";
            }
            return "true";
        }
        return "false";
    }

    @Override
    public String validateVCode(String phoneNumber, String vCode) {
        if (regValidatePhoneNum(phoneNumber)){
            int count = mobileRegisterDao.getAuthInfo(phoneNumber, vCode, CommonConstants.AuthInfoStatus.INACTIVE);
            if (count > 0){
                return "true";
            }
            return "false";
        }
        return "false";
    }

    /**
     * @function 校验手机号是否符合要求
     * @param phoneNumber
     * @return
     */
    public boolean regValidatePhoneNum(String phoneNumber){
        String regStr = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
        Pattern pattern = Pattern.compile(regStr);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
    /***************************setter注入方法****************************/
    public void setMobileRegisterDao(IMobileRegisterDao mobileRegisterDao) {
        this.mobileRegisterDao = mobileRegisterDao;
    }

    public void setLog(Log log) {
        this.log = log;
    }

    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    public void setMessageBO(MessageBO messageBO) {
        this.messageBO = messageBO;
    }

    public void setUserBO(UserBO userBO) {
        this.userBO = userBO;
    }
}
