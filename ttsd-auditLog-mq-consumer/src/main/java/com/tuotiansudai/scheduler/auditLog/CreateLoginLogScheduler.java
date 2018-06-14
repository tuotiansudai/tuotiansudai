package com.tuotiansudai.scheduler.auditLog;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.log.repository.mapper.LoginLogMapper;
import com.tuotiansudai.mq.client.model.MessageQueue;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class CreateLoginLogScheduler {
    static Logger logger = LoggerFactory.getLogger(CreateLoginLogScheduler.class);
    @Autowired
    private LoginLogMapper loginLogMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Scheduled(cron = "0 0 0 20 * ?", zone = "Asia/Shanghai")
    public void createLoginLog() {
        String newTableName = generateNewTableName();
        try {

            logger.info(String.format("create table %s begin ...", newTableName));
            loginLogMapper.createLoginLogTable(newTableName, generateOriginTableName());
            logger.info(String.format("create table %s end ...", newTableName));
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            sendSmsErrNotify(MessageFormat.format("定时任务创建{0}日志表error,异常明细:{1}", newTableName, e.getMessage()));
        }

    }

    private String generateNewTableName() {
        return String.format("login_log_%s", new DateTime().plusMonths(1).toString("yyyyMM"));
    }

    private String generateOriginTableName() {
        return String.format("login_log_%s", new DateTime().toString("yyyyMM"));
    }

    private void sendSmsErrNotify(String errMsg) {
        mqWrapperClient.sendMessage(MessageQueue.SmsFatalNotify, errMsg);
    }

}
