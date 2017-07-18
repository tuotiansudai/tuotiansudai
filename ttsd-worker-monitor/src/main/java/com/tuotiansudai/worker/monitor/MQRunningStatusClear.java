package com.tuotiansudai.worker.monitor;

import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MQRunningStatusClear {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            return;
        }
        String configFile = args[0];
        Properties properties = new Properties();
        properties.load(new FileInputStream(configFile));

        String redisHost = properties.getProperty("common.redis.host");
        int redisPort = Integer.parseInt(properties.getProperty("common.redis.port"));
        String redisPassword = properties.getProperty("common.redis.password");
        int redisDB = Integer.parseInt(properties.getProperty("common.redis.db"));

        Jedis jedis = new Jedis(redisHost, redisPort);
        if (!StringUtils.isEmpty(redisPassword)) {
            jedis.auth(redisPassword);
        }
        jedis.connect();
        jedis.select(redisDB);

        jedis.del(WorkerMonitor.HEALTH_REPORT_REDIS_KEY);
        jedis.close();
    }
}
