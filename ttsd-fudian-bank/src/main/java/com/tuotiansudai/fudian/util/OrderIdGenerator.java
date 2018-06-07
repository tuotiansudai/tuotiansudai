package com.tuotiansudai.fudian.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class OrderIdGenerator {

    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

    public static String generate(RedisTemplate<String, String> redisTemplate) {
        String key = MessageFormat.format("BANK_ORDER_NO_{0}", DATE_FORMAT.format(new Date())) ;
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        Long index = operations.increment(key, 1);
        redisTemplate.expire(key, 1, TimeUnit.DAYS);
        return String.format("%s%012d", key, index);
    }
}
