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
import com.ttsd.mobile.dao.IMobileRegisterDao;
import com.ttsd.mobile.service.IMobileRegisterService;
import com.ttsd.util.CommonUtils;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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

    @Resource(name = "userService")
    private UserService userService;

    @Resource
    private AuthService authService;

    @Resource(name = "messageBO")
    private MessageBO messageBO;

    @Resource(name = "userBO")
    private UserBO userBO;

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String mobileRegister(String userName, String password, String phoneNum, String vCode,String operationType) {
        if (userName == null || userName.equals("")){
            return "false";
        }else {
            int count = mobileRegisterDao.getUserCountByUserName(userName);
            if(count > 0){
                return "false";
            }
        }

        if (password == null || password.equals("")){
            return "false";
        }

        if (phoneNum == null || phoneNum.equals("")){
            return "false";
        }else {
            int count = mobileRegisterDao.getUserCountByCellphone(phoneNum);
            if (count > 0){
                return "false";
            }else {
                if (operationType.equals("0")) {
                    String regStr = "^((13[0-9])|(15[^4,\\\\D])|(18[0,5-9]))\\\\d{8}$";
                    Pattern pattern = Pattern.compile(regStr);
                    Matcher matcher = pattern.matcher(phoneNum);
                    if (matcher.matches()) {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("time", DateUtil.DateToString(new Date(),DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
                        params.put(
                                "authCode",
                                authService.createAuthInfo(null, phoneNum, null,
                                        CommonConstants.AuthInfoType.REGISTER_BY_MOBILE_NUMBER)
                                        .getAuthCode());
                        if(!CommonUtils.isDevEnvironment("environment")){
                            messageBO.sendSMS(mobileRegisterDao.getMessageTemplate(UserMessageTemplate.class,MessageConstants.UserMessageNodeId.REGISTER_BY_MOBILE_NUMBER + "_sms"),
                                    params, phoneNum);

                        }
                        userService.sendRegisterByMobileNumberSMS(phoneNum);
                        return "ture";
                    }
                }else if (operationType.equals("1")){
                    int codeCount = mobileRegisterDao.getAuthInfo(phoneNum,vCode,"activated");
                    if(codeCount < 2 && codeCount > 0) {

                    }
                    User user = new User();
                    user.setRegisterTime(new Date());
                    user.setUsername(userName);
                    user.setMobileNumber(phoneNum);
                    // 用户密码通过sha加密
                    user.setPassword(HashCrypt.getDigestHash(password));
                    user.setStatus(UserConstants.UserStatus.ENABLE);
                    userBO.save(user);
                    // 添加普通用户权限
                    Role role = new Role("MEMBER");
                    userBO.addRole(user, role);
                }
            }
        }
        return null;
    }


    /***************************setter注入方法****************************/
    public void setMobileRegisterDao(IMobileRegisterDao mobileRegisterDao) {
        this.mobileRegisterDao = mobileRegisterDao;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
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
