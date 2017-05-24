package com.tuotiansudai.paywrapper.ghb.listener;

import com.tuotiansudai.paywrapper.ghb.service.GHBMessageRecordService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GHBMessageRecordsListener implements InitializingBean {

    private final GHBMessageRecordService ghbMessageRecordService;

    @Autowired
    public GHBMessageRecordsListener(GHBMessageRecordService ghbMessageRecordService) {
        this.ghbMessageRecordService = ghbMessageRecordService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ghbMessageRecordService.autoCreateMessageRecordTables();
    }
}
