package com.tuotiansudai.util;

import com.tuotiansudai.client.RedisWrapperClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Random;

@Service
public class RandomUtils {

    private final static String REDIS_KEY_TEMPLATE = "webmobile:{0}:{1}:showinvestorname";

    private static final String allChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final String letterChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Value("#{'${web.random.investor.list}'.split('\\|')}")
    private List<String> showRandomLoginNameList;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    private static String generateMixString(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(allChar.charAt(random.nextInt(letterChar.length())));
        }
        return sb.toString();
    }

    public  String generateLowerString(int length) {
        return generateMixString(length).toLowerCase();
    }

    public  String generateUpperString(int length) {
        return generateMixString(length).toUpperCase();
    }

    public static String showChar(int showLength) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < showLength; i++) {
            sb.append('*');
        }
        return sb.toString();
    }

    public String encryptLoginName(String loginName, String investorLoginName, int showLength, long investId) {
        if (investorLoginName.equalsIgnoreCase(loginName)) {
            return investorLoginName;
        }

        String redisKey = MessageFormat.format(REDIS_KEY_TEMPLATE, String.valueOf(investId), investorLoginName);

        if (showRandomLoginNameList.contains(investorLoginName) && !redisWrapperClient.exists(redisKey)) {
            redisWrapperClient.set(redisKey, generateLowerString(3) + RandomUtils.showChar(showLength));
        }

        String encryptLoginName = investorLoginName.substring(0, 3) + RandomUtils.showChar(showLength);

        return redisWrapperClient.exists(redisKey) ? redisWrapperClient.get(redisKey) :encryptLoginName;
    }

}
