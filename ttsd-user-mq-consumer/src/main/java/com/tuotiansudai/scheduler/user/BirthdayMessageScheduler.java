package com.tuotiansudai.scheduler.user;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.AppUrl;
import com.tuotiansudai.enums.MessageEventType;
import com.tuotiansudai.enums.PushSource;
import com.tuotiansudai.enums.PushType;
import com.tuotiansudai.message.EventMessage;
import com.tuotiansudai.message.PushMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;

public class BirthdayMessageScheduler {

//    @Autowired
//    private UserMapper userMapper;
//
//    @Autowired
//    private MQWrapperClient mqWrapperClient;
//
//    @Scheduled(cron = "0 0 10 * * ?", zone = "Asia/Shanghai")
//    public void membershipExpiredMessage() {
//
//        List<String> birthDayUsers = userMapper.findBirthDayUsers();
//        //Title:拓天速贷为您送上生日祝福，请查收！
//        //AppTitle:拓天速贷为您送上生日祝福，请查收！
//        //Content:尊敬的{0}先生/女士，我猜今天是您的生日，拓天速贷在此送上真诚的祝福，生日当月出借即可享受收益翻倍哦！
//        String title = MessageEventType.BIRTHDAY.getTitleTemplate();
//        birthDayUsers.forEach(loginName -> {
//            String userName = userMapper.findByLoginName(loginName).getUserName();
//            String content = MessageFormat.format(MessageEventType.BIRTHDAY.getContentTemplate(), userName);
//            mqWrapperClient.sendMessage(MessageQueue.EventMessage, new EventMessage(MessageEventType.BIRTHDAY, Lists.newArrayList(loginName), title, content, null));
//            mqWrapperClient.sendMessage(MessageQueue.PushMessage, new PushMessage(Lists.newArrayList(loginName), PushSource.ALL, PushType.BIRTHDAY, title, AppUrl.MESSAGE_CENTER_LIST));
//        });
//    }
}
