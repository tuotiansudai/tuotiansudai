package com.tuotiansudai.paywrapper.ghb.service;

import com.tuotiansudai.util.RedissonManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

public abstract class GHBQueryBase {

    private static final Log logger = LogFactory.getLog(GHBQueryBase.class);

    private static final RedissonClient redisson = RedissonManager.getRedission();

    protected boolean fetchLock() {
        String lockName = this.getClass().getSimpleName();
        RLock fairLock = redisson.getFairLock(lockName);
        try {
            boolean lock = fairLock.tryLock(1, 180, TimeUnit.SECONDS);
            logger.info(MessageFormat.format("fetch {0} {1}", lockName, lock));
            return lock;
        } catch (InterruptedException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return false;
    }
}
