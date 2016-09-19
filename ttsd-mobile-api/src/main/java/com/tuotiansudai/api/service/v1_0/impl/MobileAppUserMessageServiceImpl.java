package com.tuotiansudai.api.service.v1_0.impl;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppUserMessageService;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.message.repository.mapper.UserMessageMapper;
import com.tuotiansudai.message.repository.model.MessageChannel;
import com.tuotiansudai.message.repository.model.UserMessageModel;
import com.tuotiansudai.message.service.UserMessageService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MobileAppUserMessageServiceImpl implements MobileAppUserMessageService {

    @Autowired
    private UserMessageService userMessageServices;

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Autowired
    private RedisWrapperClient redisClient;

    public static final String UNREAD_MESSAGE_COUNT_ID_KEY = "app:unread:message:count:ids:{0}";

    @Override
    public BaseResponseDto getUserMessages(UserMessagesRequestDto requestDto) {
        String loginName = LoginUserInfo.getLoginName();
        userMessageServices.generateUserMessages(loginName, MessageChannel.APP_MESSAGE);
        UserMessageResponseDataDto messageDataDto = fillMessageDataDto(loginName, requestDto.getIndex(), requestDto.getPageSize());
        BaseResponseDto<UserMessageResponseDataDto> responseDto = new BaseResponseDto<>(ReturnMessage.SUCCESS);
        responseDto.setData(messageDataDto);
        return responseDto;
    }

    @Override
    public BaseResponseDto<MobileAppUnreadMessageCount> getUnreadMessageCount(BaseParamDto baseParamDto) {
        String loginName = LoginUserInfo.getLoginName();
        long unreadMessageCount = userMessageServices.getUnreadMessageCount(loginName, MessageChannel.APP_MESSAGE);
        boolean existUnreadMessage = existUnreadMessage(loginName, unreadMessageCount);
        MobileAppUnreadMessageCount messageCount = new MobileAppUnreadMessageCount(unreadMessageCount, existUnreadMessage);
        BaseResponseDto<MobileAppUnreadMessageCount> responseDto = new BaseResponseDto<>(ReturnMessage.SUCCESS);
        responseDto.setData(messageCount);
        return responseDto;
    }

    private UserMessageResponseDataDto fillMessageDataDto(String loginName, int index, int pageSize) {
        long totalCount = userMessageMapper.countMessagesByLoginName(loginName, MessageChannel.APP_MESSAGE);
        List<UserMessageModel> userMessageModels = userMessageMapper.findMessagesByLoginName(loginName, MessageChannel.APP_MESSAGE, (index - 1) * pageSize, pageSize);
        List<UserMessageDto> userMessages = Lists.transform(userMessageModels, new Function<UserMessageModel, UserMessageDto>() {
            @Override
            public UserMessageDto apply(UserMessageModel model) {
                UserMessageDto userMessageDto = new UserMessageDto(model);
                userMessageDto.setTitle(model.getAppTitle());
                userMessageDto.setContent(StringUtils.isEmpty(model.getContent()) ? model.getAppTitle() : model.getContent());
                return userMessageDto;
            }
        });

        return new UserMessageResponseDataDto(index, pageSize, totalCount, userMessages);
    }

    private boolean existUnreadMessage(String loginName, long currentUnreadMessageCount) {
        String unreadMessageKey = MessageFormat.format(UNREAD_MESSAGE_COUNT_ID_KEY, loginName);
        String unreadMessageCountValue = redisClient.get(unreadMessageKey);

        long lastUnreadMessageCount = StringUtils.isEmpty(unreadMessageCountValue) ? 0 : Long.parseLong(unreadMessageCountValue);

        if (lastUnreadMessageCount == currentUnreadMessageCount) {
            return false;
        } else {
            redisClient.set(unreadMessageKey, String.valueOf(currentUnreadMessageCount));
            return true;
        }
    }

    @Override
    public BaseResponseDto updateReadMessage(String messageId) {
        BaseResponseDto baseDto = new BaseResponseDto();
        baseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        UserMessageModel userMessageModel = userMessageMapper.findById(Long.parseLong(messageId));
        userMessageModel.setRead(true);
        userMessageModel.setReadTime(new Date());
        userMessageMapper.update(userMessageModel);
        return baseDto;
    }
}
