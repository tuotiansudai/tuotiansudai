package com.tuotiansudai.service.impl;

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
import com.tuotiansudai.service.CheckUserBalanceService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.SendCloudTemplate;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CheckUserBalanceServiceImpl implements CheckUserBalanceService {

    private static Logger logger = Logger.getLogger(CheckUserBalanceServiceImpl.class);

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

    private static final int BATCH_SIZE = 10000;

    public void checkUserBalance() {
        logger.info("start checkUserBalance.");

        long totalCount = accountMapper.count();
        int startIndex = 0;


        List<String> mismatchUserList = Lists.newArrayList();
        List<String> failUserList = Lists.newArrayList();

        String startTime = new DateTime().toString("yyyy-MM-dd HH:mm:ss");

        while (startIndex < totalCount) {
            logger.info("checkUserBalance, run to index: " + startIndex);
            List<AccountModel> accountModelList = accountMapper.findAccountWithBalance(startIndex, BATCH_SIZE);

            for (AccountModel account : accountModelList) {
                Map<String, String> balanceMap = payWrapperClient.getUserBalance(account.getLoginName());
                if (balanceMap == null) {
                    logger.info("check user balance for user " + account.getLoginName() + " fail. skip it.");
                    failUserList.add(account.getLoginName());
                    continue;
                }
                long balance = Long.parseLong(balanceMap.get("balance"));
                if(balance != account.getBalance()) {
                    mismatchUserList.add(account.getLoginName() + "-" + account.getBalance() + "-" + balance);
                }
            }
            startIndex += BATCH_SIZE;
        }

        Map<String, Object> resultMap = Maps.newHashMap(ImmutableMap.<String, Object>builder()
                .put("failList", failUserList)
                .put("userList", mismatchUserList)
                .put("startTime", startTime)
                .put("endTime", new DateTime().toString("yyyy-MM-dd HH:mm:ss"))
                .build());

        this.sendUserBalanceCheckingResult(notifyEmailAddressList, resultMap);

        logger.info("end checkUserBalance.");
    }

    @SuppressWarnings(value = "unchecked")
    private void sendUserBalanceCheckingResult(List<String> toAddressList, Map<String, Object> map) {
        List<String> mismatchUserList = (List<String>) map.get("userList");

        Map<String, String> headerMap = Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("startTime", map.get("startTime").toString())
                .put("endTime", map.get("endTime").toString())
                .put("env", environment.name())
                .put("userCount", String.valueOf(mismatchUserList.size()))
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
