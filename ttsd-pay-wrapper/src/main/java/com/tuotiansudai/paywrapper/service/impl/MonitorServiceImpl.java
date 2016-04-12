package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.paywrapper.repository.mapper.UmpProcessListMapper;
import com.tuotiansudai.paywrapper.service.MonitorService;
import com.tuotiansudai.repository.mapper.ProcessListMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MonitorServiceImpl implements MonitorService {

    static Logger logger = Logger.getLogger(MonitorServiceImpl.class);

    @Autowired
    private ProcessListMapper processListMapper;

    @Autowired
    private UmpProcessListMapper umpProcessListMapper;

    @Override
    @Transactional
    public boolean getAADatabaseStatus() {
        try {
            return processListMapper.count() > 0;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return false;
    }

    @Override
    @Transactional(value = "payTransactionManager")
    public boolean getUMPDatabaseStatus() {
        try {
            return umpProcessListMapper.count() > 0;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return false;
    }


}
