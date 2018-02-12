package com.tuotiansudai.scheduler.loan.validator;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;

@Component
public class InvestStatusValidatorScheduler {

    private final static String INVEST_CHECK_QUEUE_KEY = "invest:check:queue";

    private final static Logger logger = Logger.getLogger(InvestStatusValidatorScheduler.class);

    private final static RedisWrapperClient client = RedisWrapperClient.getInstance();

    private final PayWrapperClient payWrapperClient;

    @Autowired
    public InvestStatusValidatorScheduler(PayWrapperClient payWrapperClient) {
        this.payWrapperClient = payWrapperClient;
    }

    @Scheduled(fixedDelay = 1000 * 60, initialDelay = 1000 * 60, zone = "Asia/Shanghai")
    public void run() {
        logger.info("[Invest Status Validator Scheduler] starting...");

        Long size = client.llen(INVEST_CHECK_QUEUE_KEY);

        if (size == null || size == 0) {
            logger.info("[Invest Status Validator Scheduler] size is 0, done");
            return;
        }

        for (long index = 0; index < size; index++) {
            List<String> investOrderIds = client.lrange(INVEST_CHECK_QUEUE_KEY, -1, -1);
            String orderId = investOrderIds.get(0);
            try {
                BaseDto<PayDataDto> dto = payWrapperClient.investStatusValidate(MessageFormat.format("/transaction-status/invest/{0}", orderId));

                if (dto.isSuccess()) {
                    logger.info(MessageFormat.format("[Invest Status Validator Scheduler] invest {0} status is {1}",
                            orderId,
                            dto.getData().getExtraValues().getOrDefault("transferStatus", "")));

                    if (dto.getData().getStatus()) {
                        client.rpop(INVEST_CHECK_QUEUE_KEY);
                    } else {
                        client.lpush(INVEST_CHECK_QUEUE_KEY, client.rpop(INVEST_CHECK_QUEUE_KEY));
                    }
                } else {
                    logger.warn("[Invest Status Validator Scheduler] connection error");
                }
            } catch (Exception e) {
                logger.warn(e.getLocalizedMessage(), e);
            }
        }

        logger.info(MessageFormat.format("[Invest Status Validator Scheduler] size is {0} done", String.valueOf(size)));
    }
}
