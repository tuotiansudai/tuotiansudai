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

    public String encryptMobileForCurrentLoginName(String loginName, String encryptLoginName, Long investId, Source source) {
        String  originalMobile = userMapper.findByLoginName(encryptLoginName).getMobile();

        if (encryptLoginName.equalsIgnoreCase(loginName)) {
            return originalMobile;
        }

        String originalOrFakeMobile = this.getOriginalOrFakeMobile(investId, originalMobile);

        return source.equals(Source.WEB) ? MobileEncryptor.encryptWebMiddleMobile(originalOrFakeMobile) : MobileEncryptor.encryptAppMiddleMobile(originalOrFakeMobile);
    }

    private String getOriginalOrFakeMobile(Long investId, String originalMobile) {
        if (investId == null) {
            return originalMobile;
        }

        String redisKey = MessageFormat.format(REDIS_KEY_TEMPLATE, String.valueOf(investId), originalMobile);

        if (showRandomLoginNameList.contains(originalMobile) && !redisWrapperClient.exists(redisKey)) {
            redisWrapperClient.set(redisKey, originalMobile.substring(0, 3) + MobileEncryptor.showChar(4) + generateNumString());
        }
        return redisWrapperClient.exists(redisKey) ? redisWrapperClient.get(redisKey) : originalMobile;
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
