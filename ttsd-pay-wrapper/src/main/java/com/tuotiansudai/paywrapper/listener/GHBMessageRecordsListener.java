package com.tuotiansudai.paywrapper.listener;

import com.tuotiansudai.paywrapper.service.GHBMessageRecordService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GHBMessageRecordsListener implements InitializingBean {
    @Autowired
    private GHBMessageRecordService ghbMessageRecordService;

    @Override
    public void afterPropertiesSet() throws Exception {
        ghbMessageRecordService.autoCreateMessageRecordTables();
    }
}
