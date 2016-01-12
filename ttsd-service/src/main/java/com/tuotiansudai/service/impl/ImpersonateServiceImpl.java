package com.tuotiansudai.service.impl;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.security.MyAuthenticationManager;
import com.tuotiansudai.service.ImpersonateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImpersonateServiceImpl implements ImpersonateService {

    @Autowired
    private MyAuthenticationManager myAuthenticationManager;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    private final String IMPERSONATE_SECURITY_KEY = "impersonate:";

    @Override
    public boolean impersonateLogin(String loginName, String randomCode) {

        String cashedCode = redisWrapperClient.get(IMPERSONATE_SECURITY_KEY + loginName);

        if (StringUtils.isNotEmpty(cashedCode) && cashedCode.equals(randomCode)) {
            myAuthenticationManager.createAuthentication(loginName);
            return true;
        }
        return false;
    }

    @Override
    public String plantRandomCode(String loginName) {
        String randomCode = generateRandomCode();
        redisWrapperClient.setex(IMPERSONATE_SECURITY_KEY + loginName, 5, randomCode);
        return randomCode;
    }

    private String generateRandomCode() {
        return String.valueOf((int) (Math.random() * 1000000));
    }
}
