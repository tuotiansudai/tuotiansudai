package com.tuotiansudai.service.impl;

import com.tuotiansudai.service.ImpersonateService;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;


@Service
public class ImpersonateServiceImpl implements ImpersonateService {

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private static Logger logger = Logger.getLogger(ImpersonateServiceImpl.class);

    private final String IMPERSONATE_SECURITY_KEY = "impersonate:security:";

    @Override
    public String impersonateLogin(String securityCode) {
        String loginName = redisWrapperClient.get(IMPERSONATE_SECURITY_KEY + securityCode);
        if (StringUtils.isNotEmpty(loginName)) {
            logger.info("impersonate login, securityCode: " + securityCode + ", user login name: " + loginName);
            redisWrapperClient.del(IMPERSONATE_SECURITY_KEY + securityCode);
            return loginName;
        }
        logger.warn("impersonate login fail, securityCode: " + securityCode + ", user login name is empty.");
        return null;
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
