package com.tuotiansudai.util;

import com.tuotiansudai.repository.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Random;

@Service
public class RandomUtils {

    private final static String REDIS_KEY_TEMPLATE = "webmobile:{0}:{1}:showinvestorname";

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private static final String numberChar = "0123456789";

    @Value("#{'${web.random.investor.list}'.split('\\|')}")
    private List<String> showRandomLoginNameList;

    @Autowired
    private UserMapper userMapper;

    private String generateNumString() {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            stringBuilder.append(numberChar.charAt(random.nextInt(numberChar.length())));
        }
        return stringBuilder.toString();
    }

    public String encryptMobile(String loginName, String investorLoginName, long investId) {
        String userMobile;
        String investUserMobile = userMapper.findByLoginName(investorLoginName).getMobile();
        if (StringUtils.isNotEmpty(loginName)) {
            userMobile = userMapper.findByLoginName(loginName).getMobile();
            if (investUserMobile.equalsIgnoreCase(userMobile)) {
                return investUserMobile;
            }
        }
        String redisKey = MessageFormat.format(REDIS_KEY_TEMPLATE, String.valueOf(investId), investUserMobile);
        if (showRandomLoginNameList.contains(investorLoginName) && !redisWrapperClient.exists(redisKey)) {
            redisWrapperClient.set(redisKey, investUserMobile.substring(0, 3) + MobileEncryptor.showChar(4) + generateNumString());
        }
        String encryptMobile = redisWrapperClient.exists(redisKey) ? redisWrapperClient.get(redisKey) : investUserMobile;
        return MobileEncryptor.encryptMiddleMobile(encryptMobile);
    }

    public String encryptMobile(String loginName, String encryptLoginName) {
        if (encryptLoginName.equalsIgnoreCase(loginName)) {
            return userMapper.findByLoginName(loginName).getMobile();
        }

        return MobileEncryptor.encryptMiddleMobile(userMapper.findByLoginName(encryptLoginName).getMobile());
    }
}