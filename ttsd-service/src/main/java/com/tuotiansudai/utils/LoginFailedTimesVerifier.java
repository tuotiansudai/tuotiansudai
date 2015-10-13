package com.tuotiansudai.utils;

import com.tuotiansudai.client.RedisWrapperClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * Created by Administrator on 2015/10/12.
 */
@Component
public class LoginFailedTimesVerifier {

    @Value("${login.max.times}")
    private int times;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    public boolean loginFailedTimesVerifier(String loginName) {
        String redisKey = MessageFormat.format("web:{0}:loginfailedtimes", loginName);
        if (redisWrapperClient.exists(redisKey) && Integer.parseInt(redisWrapperClient.get(redisKey)) == times) {
            return false;
        } else {
            return true;
        }
    }

}
