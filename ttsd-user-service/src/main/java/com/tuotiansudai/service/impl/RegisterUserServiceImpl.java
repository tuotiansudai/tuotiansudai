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


        mqWrapperClient.sendMessage(MessageQueue.GenerateReferrerRelation, userModel.getLoginName());

    }
}
