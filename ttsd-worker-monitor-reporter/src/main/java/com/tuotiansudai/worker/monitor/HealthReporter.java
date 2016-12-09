package com.tuotiansudai.worker.monitor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import redis.clients.jedis.Jedis;

import java.util.Timer;
import java.util.TimerTask;


public abstract class HealthReporter implements InitializingBean {
    static final String WORKER_HEALTH_WORKER_STATUS_REDIS_KEY_TEMPLATE = "worker:health:report:%s";
    static final int REPORT_INTERVAL_SECONDS = 10;

    private static final Logger logger = Logger.getLogger(HealthReporter.class);
    private final Timer reportTimer;

    private Jedis jedis;
    private String workerHealthRedisKey;
    private int lifeLong = 1;

    protected abstract JobWorker getCurrentJobWorker();

    protected abstract Jedis getJedis();

    protected HealthReporter() {
        this.reportTimer = new Timer();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.jedis = getJedis();

        this.workerHealthRedisKey = String.format(WORKER_HEALTH_WORKER_STATUS_REDIS_KEY_TEMPLATE, getCurrentJobWorker());

        if (logger.isDebugEnabled()) {
            logger.debug("start health status check for " + getCurrentJobWorker());
        }

        reportTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                reportStatus();
            }
        }, REPORT_INTERVAL_SECONDS, REPORT_INTERVAL_SECONDS * 1000);
    }

    private void reportStatus() {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("%s is working", getCurrentJobWorker()));
        }
        jedis.setex(workerHealthRedisKey, REPORT_INTERVAL_SECONDS * 10, String.valueOf(lifeLong++));
    }
}
