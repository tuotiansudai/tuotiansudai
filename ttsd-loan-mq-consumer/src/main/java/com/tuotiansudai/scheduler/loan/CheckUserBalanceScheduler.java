package com.tuotiansudai.scheduler.loan;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.message.EMailMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.DateConvertUtil;
import com.tuotiansudai.util.RedisWrapperClient;
import com.tuotiansudai.util.SendCloudTemplate;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CheckUserBalanceScheduler {
    private static Logger logger = LoggerFactory.getLogger(CheckUserBalanceScheduler.class);

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Value("${common.environment}")
    private Environment environment;

    @Value("#{'${check.user.balance.notify.email}'.split('\\|')}")
    private List<String> notifyEmailAddressList;

    private static final int BATCH_SIZE = 50000;

    private static final String LAST_CHECK_USER_BALANCE_TIME = "last_check_user_balance_time";

    private static final int LEFT_SECOND = 60 * 60 * 24 * 90;

    private static final int RETRY_TIMES = 6;

    //    @Scheduled(cron = "0 30 1 * * SUN,SAT", zone = "Asia/Shanghai")
    @Scheduled(cron = "0 18 15 * * *", zone = "Asia/Shanghai")
    public void checkUserBalance() {
        logger.info("[checkUserBalance:] start .");
//        if (Environment.PRODUCTION != environment) {
//            return;
//        }
        try {

            List<String> mismatchUserList = Lists.newArrayList();
            List<String> failUserList = Lists.newArrayList();

            String startTime = new DateTime().toString("yyyy-MM-dd HH:mm:ss");
            String lastCheckUserBalanceTime = redisWrapperClient.exists(LAST_CHECK_USER_BALANCE_TIME) ? redisWrapperClient.get(LAST_CHECK_USER_BALANCE_TIME) : null;
            List<AccountModel> accountModelList = accountMapper.findAccountWithBalance(lastCheckUserBalanceTime, BATCH_SIZE);
            int accountModelCount = accountModelList.size();
            logger.info("[checkUserBalance:] cycle start lastCheckUserBalanceTime-{},size-{}", lastCheckUserBalanceTime, accountModelList.size());
            int count = 0;
            while (count++ < accountModelCount) {
                AccountModel account = accountModelList.get(count - 1);
                logger.info("[checkUserBalance:]run to id:{} ", String.valueOf(account.getId()));

                // 如果连接umpay失败，共尝试3次
                int triedTimes = 1;
                Map<String, String> balanceMap;
                do {
                    balanceMap = payWrapperClient.getUserBalance(account.getLoginName());
                    if (balanceMap == null) {
                        logger.info("[checkUserBalance:]check user balance for user {} fail. triedTimes:{}.", account.getLoginName(), triedTimes);
                    }
                } while (balanceMap == null && triedTimes++ < RETRY_TIMES);

                if (balanceMap == null) {
                    logger.info("[checkUserBalance:]check user balance for user {} fail after retry {} times. skip it.", account.getLoginName(), RETRY_TIMES);
                    redisWrapperClient.setex(LAST_CHECK_USER_BALANCE_TIME, LEFT_SECOND, DateConvertUtil.format(account.getRegisterTime(), "yyyy-MM-dd HH:mm:ss"));
                    failUserList.add(account.getLoginName()); // 重试3次都失败，加入连接失败的名单中
                    continue;
                }

                long balance = Long.parseLong(balanceMap.get("balance"));
                if (balance != account.getBalance()) {
                    mismatchUserList.add(account.getLoginName() + "-" + account.getBalance() + "-" + balance);
                }
                if (count == BATCH_SIZE) {
                    String lastRecordRegisterTime = DateConvertUtil.format(account.getRegisterTime(), "yyyy-MM-dd HH:mm:ss");
                    logger.info("[checkUserBalance:] last record register time-{},id-{}", lastRecordRegisterTime, String.valueOf(account.getId()));
                    redisWrapperClient.setex(LAST_CHECK_USER_BALANCE_TIME, LEFT_SECOND, lastRecordRegisterTime);
                }
            }
            logger.info("[checkUserBalance:] cycle end lastCheckUserBalanceTime-{},size-{}", lastCheckUserBalanceTime, accountModelList.size());

            if (accountModelCount > 0 && accountModelCount < BATCH_SIZE) {
                logger.info("[checkUserBalance:] del key last record register time-{},id-{}",
                        DateConvertUtil.format(accountModelList.get(accountModelCount - 1).getRegisterTime(), "yyyy-MM-dd HH:mm:ss"),
                        String.valueOf(accountModelList.get(accountModelCount - 1).getId()));
                redisWrapperClient.del(LAST_CHECK_USER_BALANCE_TIME);
            }

            Map<String, Object> resultMap = Maps.newHashMap(ImmutableMap.<String, Object>builder()
                    .put("failList", failUserList)
                    .put("userList", mismatchUserList)
                    .put("totalUserCount", count - 1)
                    .put("startTime", startTime)
                    .put("endTime", new DateTime().toString("yyyy-MM-dd HH:mm:ss"))
                    .build());

            this.sendUserBalanceCheckingResult(notifyEmailAddressList, resultMap);
        } catch (Exception e) {
            logger.error("[checkUserBalance] job execution is failed.", e);
        }


        logger.info("[checkUserBalance:] end .");
    }

    @SuppressWarnings(value = "unchecked")
    private void sendUserBalanceCheckingResult(List<String> toAddressList, Map<String, Object> map) {
        List<String> mismatchUserList = (List<String>) map.get("userList");

        Map<String, String> headerMap = Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("startTime", map.get("startTime").toString())
                .put("endTime", map.get("endTime").toString())
                .put("env", environment.name())
                .put("totalUserCount", String.valueOf(map.get("totalUserCount")))
                .put("mismatchUserCount", String.valueOf(mismatchUserList.size()))
                .build());
        String contentHeader = SendCloudTemplate.USER_BALANCE_CHECK_RESULT_HEADER.generateContent(headerMap);

        StringBuilder bodySb = new StringBuilder();
        for (String userInfo : mismatchUserList) {
            String[] userInfoArr = userInfo.split("-");
            Map<String, String> bodyLineMap = new HashMap<>();
            bodyLineMap.put("loginName", userInfoArr[0]);
            bodyLineMap.put("ttsdBalance", AmountConverter.convertCentToString(Long.parseLong(userInfoArr[1])));
            bodyLineMap.put("umpayBalance", AmountConverter.convertCentToString(Long.parseLong(userInfoArr[2])));
            bodySb.append(SendCloudTemplate.USER_BALANCE_CHECK_RESULT_BODY.generateContent(bodyLineMap));
        }

        StringBuilder contentTail = new StringBuilder(SendCloudTemplate.USER_BALANCE_CHECK_RESULT_TAIL.getTemplate());
        List<String> failList = (List<String>) map.get("failList");
        contentTail.append("</br>");
        contentTail.append("<div>连接失败的用户：");
        failList.forEach(fail -> contentTail.append(fail).append(", "));
        contentTail.append("</div>");

        String title = "[" + environment.name() + "] " + SendCloudTemplate.USER_BALANCE_CHECK_RESULT_BODY.getTitle();
        String content = contentHeader + bodySb.toString() + contentTail.toString();

        mqWrapperClient.sendMessage(MessageQueue.EMailMessage, new EMailMessage(toAddressList, title, content));
    }
}

