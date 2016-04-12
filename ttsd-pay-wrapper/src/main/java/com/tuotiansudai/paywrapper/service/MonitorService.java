package com.tuotiansudai.paywrapper.service;

import org.springframework.transaction.annotation.Transactional;

public interface MonitorService {
    @Transactional
    boolean getAADatabaseStatus();

    @Transactional(value = "payTransactionManager")
    boolean getUMPDatabaseStatus();
}
