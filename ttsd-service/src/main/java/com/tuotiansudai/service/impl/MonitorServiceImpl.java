package com.tuotiansudai.service.impl;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.repository.mapper.ProcessListMapper;
import com.tuotiansudai.service.MonitorService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MonitorServiceImpl implements MonitorService {

    static Logger logger = Logger.getLogger(MonitorServiceImpl.class);

    @Autowired
    private ProcessListMapper processListMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Override
    public boolean getDatabaseStatus() {
        try {
            return processListMapper.count() > 0;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return false;
    }

    @Override
    public boolean getRedisStatus() {
        try {
            redisWrapperClient.set("checkHealth", "true");
            return redisWrapperClient.del("checkHealth");
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return false;
    }
}
