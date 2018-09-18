package com.tuotiansudai.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.SmsNotifyDto;
import com.tuotiansudai.dto.request.RegisterRequestDto;
import com.tuotiansudai.dto.response.UserRestUserInfo;
import com.tuotiansudai.enums.*;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.message.EventMessage;
import com.tuotiansudai.message.ExperienceAssigningMessage;
import com.tuotiansudai.message.PushMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.UserRestClient;
import com.tuotiansudai.service.RegisterUserService;
import com.tuotiansudai.util.RedisWrapperClient;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@Service
public class RegisterUserServiceImpl implements RegisterUserService {

    @Autowired
    private UserRestClient userRestClient;

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    private static final long EXPERIENCE_AMOUNT = 688800L;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserModel register(RegisterRequestDto registerDto) {
        if (!Strings.isNullOrEmpty(registerDto.getChannel()) && registerDto.getChannel().length() > 32) {
            registerDto.setChannel(registerDto.getChannel().substring(0, 32));
        }
        UserRestUserInfo registerUserInfo = userRestClient.register(registerDto);
        UserModel userModel = registerUserInfo.getUserInfo().toUserModel();

        MembershipModel membershipModel = membershipMapper.findByLevel(0);
        UserMembershipModel userMembershipModel = UserMembershipModel.createUpgradeUserMembershipModel(userModel.getLoginName(), membershipModel.getId());
        userMembershipMapper.create(userMembershipModel);

        this.sendMessage(userModel);

        mqWrapperClient.sendMessage(MessageQueue.SmsNotify, new SmsNotifyDto(JianZhouSmsTemplate.SMS_REGISTER_SUCCESS_TEMPLATE, Lists.newArrayList(userModel.getMobile())));

        return userModel;
    }

    private void sendMessage(UserModel userModel) {
        //发放体验金
        mqWrapperClient.sendMessage(MessageQueue.ExperienceAssigning,
                new ExperienceAssigningMessage(userModel.getLoginName(), EXPERIENCE_AMOUNT, ExperienceBillOperationType.IN, ExperienceBillBusinessType.REGISTER));

        mqWrapperClient.sendMessage(MessageQueue.UserRegistered_CompletePointTask, userModel.getLoginName());

        //Title:5888元体验金已存入您的账户，请查收！
        //Content:哇，您终于来啦！初次见面，岂能无礼？5888元体验金双手奉上，【立即体验】再拿588元红包和3%加息券！
        mqWrapperClient.sendMessage(MessageQueue.EventMessage, new EventMessage(MessageEventType.REGISTER_USER_SUCCESS,
                Lists.newArrayList(userModel.getLoginName()),
                MessageEventType.REGISTER_USER_SUCCESS.getTitleTemplate(),
                MessageEventType.REGISTER_USER_SUCCESS.getContentTemplate(),
                null));

        mqWrapperClient.sendMessage(MessageQueue.GenerateReferrerRelation, userModel.getLoginName());

        if (!Strings.isNullOrEmpty(userModel.getReferrer())) {
            //Title:您推荐的好友 {0} 已成功注册
            //AppTitle:您推荐的好友 {0} 已成功注册
            //Content:尊敬的用户，您推荐的好友 {0} 已成功注册，【邀请好友投资】您还能再拿1%现金奖励哦！
            mqWrapperClient.sendMessage(MessageQueue.EventMessage, new EventMessage(MessageEventType.RECOMMEND_SUCCESS,
                    Lists.newArrayList(userModel.getReferrer()),
                    MessageFormat.format(MessageEventType.RECOMMEND_SUCCESS.getTitleTemplate(), userModel.getMobile()),
                    MessageFormat.format(MessageEventType.RECOMMEND_SUCCESS.getContentTemplate(), userModel.getMobile()),
                    null));

            mqWrapperClient.sendMessage(MessageQueue.PushMessage, new PushMessage(Lists.newArrayList(userModel.getReferrer()),
                    PushSource.ALL,
                    PushType.RECOMMEND_SUCCESS,
                    MessageFormat.format(MessageEventType.RECOMMEND_SUCCESS.getTitleTemplate(), userModel.getMobile()),
                    AppUrl.MESSAGE_CENTER_LIST));
        }
    }
}
