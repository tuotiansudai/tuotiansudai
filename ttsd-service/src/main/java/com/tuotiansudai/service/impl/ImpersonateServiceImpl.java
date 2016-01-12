package com.tuotiansudai.service.impl;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.security.MyAuthenticationManager;
import com.tuotiansudai.service.ImpersonateService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ImpersonateServiceImpl implements ImpersonateService {

    private static Logger logger = Logger.getLogger(ImpersonateServiceImpl.class);

    @Autowired
    private MyAuthenticationManager myAuthenticationManager;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    private final String IMPERSONATE_SECURITY_KEY = "impersonate:security:";

    @Override
    public boolean impersonateLogin(String securityCode) {

        String loginName = redisWrapperClient.get(IMPERSONATE_SECURITY_KEY + securityCode);

        if (StringUtils.isNotEmpty(loginName)) {
            logger.info("impersonate login, securityCode: " + securityCode + ", user login name: " + loginName);
            myAuthenticationManager.createAuthentication(loginName);
            redisWrapperClient.del(IMPERSONATE_SECURITY_KEY + securityCode);
            return true;
        } else {
            logger.warn("impersonate login fail, securityCode: " + securityCode + ", user login name is empty.");
            return false;
        }
    }

    @Override
    public String plantSecurityCode(String adminLoginName, String loginName) {
        logger.info("plant security code for impersonate login, admin user: " + adminLoginName + ", impersonate user: " + loginName);

        String securityCode = generateSecurityCode();
        redisWrapperClient.setex(IMPERSONATE_SECURITY_KEY + securityCode, 5, loginName);
        return securityCode;
    }

    private String generateSecurityCode() {
        return String.valueOf((int) (Math.random() * 1000000));
    }
}
