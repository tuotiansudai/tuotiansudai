package com.tuotiansudai.util;

import com.google.common.base.Strings;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class RedissonManager {

    private static RedissonClient redisson = Redisson.create(generateConfig());

    private RedissonManager() {}

    public static RedissonClient getRedission() {
        return redisson;
    }

    private static Config generateConfig() {
        ResourceBundle bundle = ResourceBundle.getBundle("ttsd-env");

        String redisHost = bundle.getString("common.redis.host");
        String redisPort = bundle.getString("common.redis.port");
        int database = Integer.parseInt(bundle.getString("common.redis.db"));
        String password = bundle.getString("common.redis.password");
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress(MessageFormat.format("{0}:{1}", redisHost, redisPort)).setDatabase(database);
        if (!Strings.isNullOrEmpty(password)) {
            singleServerConfig.setPassword(password);
        }

        return config;
    }
}
