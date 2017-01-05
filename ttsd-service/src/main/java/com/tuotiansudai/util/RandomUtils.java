package com.tuotiansudai.util;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.Source;
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

    private static final String numberChar = "0123456789";

    @Value("#{'${web.random.investor.list}'.split('\\|')}")
    private List<String> showRandomLoginNameList;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private UserMapper userMapper;

    public String encryptMobileForCurrentLoginName(String loginName, String investorLoginName, long investId, Source source) {
        String investorMobile = userMapper.findByLoginName(investorLoginName).getMobile();

        if (investorLoginName.equalsIgnoreCase(loginName)) {
            return investorMobile;
        }

        return source.equals(Source.WEB) ? MobileEncryptor.encryptWebMiddleMobile(encryptMobile) : MobileEncryptor.encryptAppMiddleMobile(encryptMobile)
    }

    public String encryptMobileForCurrentLoginName(String loginName, String encryptLoginName, Source source) {
        if (encryptLoginName.equalsIgnoreCase(loginName)) {
            return userMapper.findByLoginName(loginName).getMobile();
        }

        String mobile = userMapper.findByLoginName(encryptLoginName).getMobile();

        return source.equals(Source.WEB) ? MobileEncryptor.encryptWebMiddleMobile(mobile) : MobileEncryptor.encryptAppMiddleMobile(mobile)
    }

    private String getOriginalOrFakeMobile(long investId, String originalMobile) {
        String redisKey = MessageFormat.format(REDIS_KEY_TEMPLATE, String.valueOf(investId), originalMobile);

        if (showRandomLoginNameList.contains(investorLoginName) && !redisWrapperClient.exists(redisKey)) {
            redisWrapperClient.set(redisKey, investorMobile.substring(0, 3) + MobileEncryptor.showChar(4) + generateNumString());
        }
        String encryptMobile = redisWrapperClient.exists(redisKey) ? redisWrapperClient.get(redisKey) : investorMobile;
    }

    private String generateNumString() {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            stringBuilder.append(numberChar.charAt(random.nextInt(numberChar.length())));
        }
        return stringBuilder.toString();
    }
}
