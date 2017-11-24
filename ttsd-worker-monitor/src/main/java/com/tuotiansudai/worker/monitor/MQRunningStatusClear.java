package com.tuotiansudai.worker.monitor;

import com.tuotiansudai.etcd.ETCDConfigReader;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import java.io.IOException;

public class MQRunningStatusClear {
    public static void main(String[] args) throws IOException {
        ETCDConfigReader etcdConfigReader = ETCDConfigReader.getReader();
        String redisHost = etcdConfigReader.getValue("common.redis.host");
        int redisPort = Integer.parseInt(etcdConfigReader.getValue("common.redis.port"));
        String redisPassword = etcdConfigReader.getValue("common.redis.password");
        int redisDB = Integer.parseInt(etcdConfigReader.getValue("common.redis.db"));

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
