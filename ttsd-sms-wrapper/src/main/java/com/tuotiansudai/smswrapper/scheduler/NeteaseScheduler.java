package com.tuotiansudai.smswrapper.scheduler;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.smswrapper.SmsTemplate;
import com.tuotiansudai.smswrapper.provider.SmsProvider;
import com.tuotiansudai.smswrapper.repository.mapper.NeteaseCallbackRequestMapper;
import com.tuotiansudai.smswrapper.repository.mapper.SmsHistoryMapper;
import com.tuotiansudai.smswrapper.repository.model.NeteaseCallbackRequestModel;
import com.tuotiansudai.smswrapper.repository.model.SmsHistoryModel;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Component
public class NeteaseScheduler {

    private final static Logger logger = Logger.getLogger(NeteaseScheduler.class);

    private final static RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private final SmsHistoryMapper smsHistoryMapper;

    private final NeteaseCallbackRequestMapper neteaseCallbackRequestMapper;

    private final SmsProvider smsProviderAlidayu;

    @Autowired
    public NeteaseScheduler(@Qualifier("smsProviderAlidayu") SmsProvider smsProviderAlidayu,
                            SmsHistoryMapper smsHistoryMapper,
                            NeteaseCallbackRequestMapper neteaseCallbackRequestMapper) {
        this.neteaseCallbackRequestMapper = neteaseCallbackRequestMapper;
        this.smsHistoryMapper = smsHistoryMapper;
        this.smsProviderAlidayu = smsProviderAlidayu;
    }

    @Scheduled(initialDelay = 1000 * 10, fixedDelay = 1000 * 5)
    public void schedule() {
        logger.info("Netease scheduler trigger...");

        try {
            List<NeteaseCallbackRequestModel> failedRequests = this.neteaseCallbackRequestMapper.findFailedRequests(new Date());
            for (NeteaseCallbackRequestModel failedRequest : failedRequests) {
                logger.info(MessageFormat.format("netease failed request: {0}", failedRequest.toString()));

                String redisKey = MessageFormat.format("sendid:{0}", failedRequest.getSendid());

                try {
                    SmsTemplate smsTemplate = SmsTemplate.valueOf(redisWrapperClient.hget(redisKey, "template"));
                    String params = redisWrapperClient.hget(redisKey, "params");
                    List<String> paramList = Strings.isNullOrEmpty(params) ? Lists.newArrayList() : Lists.newArrayList(params.split("\\|"));

                    List<SmsHistoryModel> smsHistoryModels = this.smsProviderAlidayu.sendSMS(Lists.newArrayList(failedRequest.getMobile()), smsTemplate, paramList);
                    SmsHistoryModel smsHistoryModel = this.smsHistoryMapper.findById(failedRequest.getSmsHistoryId());
                    smsHistoryModel.setBackupId(smsHistoryModels.get(0).getId());
                    this.smsHistoryMapper.update(smsHistoryModel);

                    logger.info(MessageFormat.format("netease failed request send alidayu finished: {0}", failedRequest.toString()));
                } catch (Exception e) {
                    logger.error(MessageFormat.format("netease failed request send alidayu failed: {0}", failedRequest.toString()), e);
                }
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        logger.info("Netease scheduler trigger finished");
    }
}
