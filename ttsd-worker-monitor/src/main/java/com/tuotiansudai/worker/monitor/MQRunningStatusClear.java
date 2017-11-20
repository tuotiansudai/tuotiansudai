package com.tuotiansudai.worker.monitor;

import com.tuotiansudai.util.ETCDConfigReader;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MQRunningStatusClear {
    public static void main(String[] args) throws IOException {
        String redisHost = ETCDConfigReader.getValue("common.redis.host");
        int redisPort = Integer.parseInt(ETCDConfigReader.getValue("common.redis.port"));
        String redisPassword = ETCDConfigReader.getValue("common.redis.password");
        int redisDB = Integer.parseInt(ETCDConfigReader.getValue("common.redis.db"));

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
