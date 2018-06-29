package com.tuotiansudai.scheduler.loan;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.fudian.download.QueryDownloadLogFilesType;
import com.tuotiansudai.message.EMailMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.util.RedisWrapperClient;
import com.tuotiansudai.util.SendCloudTemplate;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

@Component
public class QueryDownloadFilesScheduler {

    @Autowired
    private MQWrapperClient mqWrapperClient;

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private static final String EMAIL_CONTENT_MESSAGE = "EMAIL_CONTENT_MESSAGE:{0}";

    private static final String EMAIL_CONTENT_SIZE = "EMAIL_CONTENT_SIZE:{0}:{1}";

    @Value("${common.environment}")
    private Environment environment;

    @Scheduled(cron = "0 8 * * * ?", zone = "Asia/Shanghai")
    public void sendEmailMessage() {

        String queryDate = DateTime.now().minusDays(1).toString("yyyyMMdd");
        Map<String, String> map = redisWrapperClient.hgetAll(MessageFormat.format(EMAIL_CONTENT_MESSAGE, queryDate));
        StringBuilder contentBody = new StringBuilder();

        List<QueryDownloadLogFilesType> types = Lists.newArrayList(QueryDownloadLogFilesType.values());
        types.forEach(type -> {
            String sizeKey = MessageFormat.format(EMAIL_CONTENT_SIZE, queryDate, type.name());
            String header = SendCloudTemplate.FUDIAN_CHECK_RESULT_HEADER.generateContent(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("title", type.getDescribe())
                .put("count", redisWrapperClient.exists(sizeKey) ? "0" : redisWrapperClient.get(sizeKey))
                .build()));
            String content = map.get(type.name());
            contentBody.append(header).append(Strings.isNullOrEmpty(content) ? MessageFormat.format("<h2>{0}查询失败<h2></br>", type.getDescribe()) : content).append(SendCloudTemplate.FUDIAN_CHECK_RESULT_TAIL.getTemplate());
        });

        mqWrapperClient.sendMessage(MessageQueue.EMailMessage, new EMailMessage(Maps.newHashMap(ImmutableMap.<Environment, List<String>>builder()
                .put(Environment.PRODUCTION, Lists.newArrayList("dev@tuotiansudai.com"))
                .put(Environment.QA1, Lists.newArrayList("zhujiajun@tuotiansudai.com"))
                .put(Environment.QA2, Lists.newArrayList("zhujiajun@tuotiansudai.com"))
                .put(Environment.QA3, Lists.newArrayList("zhujiajun@tuotiansudai.com"))
                .put(Environment.QA4, Lists.newArrayList("zhujiajun@tuotiansudai.com"))
                .put(Environment.QA5, Lists.newArrayList("zhujiajun@tuotiansudai.com"))
                .put(Environment.DEV, Lists.newArrayList("zhukun@tuotiansudai.com"))
                .build()).get(environment), MessageFormat.format("{0}富滇银行{1}对账信息", environment.name(), queryDate), contentBody.toString()));
    }
}
