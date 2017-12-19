package com.tuotiansudai.paywrapper.validation;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

@Component
public class ValidationScheduler {

    private final static Logger logger = Logger.getLogger(ValidationScheduler.class);

    private final static RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Resource(name = "investRepayDailyValidation")
    private DailyValidation investRepayDailyValidation;

    @Resource(name = "redEnvelopDailyValidation")
    private DailyValidation redEnvelopDailyValidation;

    @Resource(name = "extraRateDailyValidation")
    private DailyValidation extraRateDailyValidation;

    @Resource(name = "investDailyValidation")
    private DailyValidation investDailyValidation;

    @Resource(name = "couponRepayDailyValidation")
    private DailyValidation couponRepayDailyValidation;

    @Resource(name = "referrerRewardDailyValidation")
    private DailyValidation referrerRewardDailyValidation;

    private final ValidationReportSender validationReportSender;

    @Autowired
    public ValidationScheduler(ValidationReportSender validationReportSender) {
        this.validationReportSender = validationReportSender;
    }

    @Scheduled(cron = "0 30 0 * * ?", zone = "Asia/Shanghai")
    public void run() {
        try {
            long value = redisWrapperClient.incrEx("daily-validation-lock", 12 * 60 * 60);
            if (value > 1) {
                return;
            }

            logger.info("[Validation Scheduler] Starting ...");
            List<DailyValidation> validators = Lists.newArrayList(investRepayDailyValidation,
                    redEnvelopDailyValidation,
                    extraRateDailyValidation,
                    investDailyValidation,
                    couponRepayDailyValidation,
                    referrerRewardDailyValidation);

            Map<String, Object> context = Maps.newHashMap();

            for (DailyValidation validator : validators) {
                ValidationReport report = validator.validate();
                context.put(report.getMustacheContext(), report);
            }

            Mustache compile = new DefaultMustacheFactory().compile("validation-report.html");
            StringWriter writer = new StringWriter();
            compile.execute(writer, context);

            validationReportSender.send(writer.toString());

            logger.info("[Validation Scheduler] Done");
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }
}
