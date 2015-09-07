package com.tuotiansudai.smswrapper.service.impl;

import com.tuotiansudai.smswrapper.repository.mapper.SmsProcessListMapper;
import com.tuotiansudai.smswrapper.service.MonitorService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MonitorServiceImpl implements MonitorService {

    static Logger logger = Logger.getLogger(MonitorServiceImpl.class);

    @Autowired
    private SmsProcessListMapper smsProcessListMapper;

    @Override
    public boolean getDatabaseStatus() {
        try {
            return smsProcessListMapper.count() > 0;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return false;
    }
}
