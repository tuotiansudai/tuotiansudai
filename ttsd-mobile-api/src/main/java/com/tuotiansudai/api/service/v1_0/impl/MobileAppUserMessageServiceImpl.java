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
        String loginName = requestDto.getBaseParam().getUserId();
        userMessageServices.generateUserMessages(loginName);
        int index = requestDto.getIndex();
        int pageSize = requestDto.getPageSize();
        UserMessageResponseDataDto messageDataDto = fillMessageDataDto(loginName, index, pageSize);
        BaseResponseDto responseDto = new BaseResponseDto<>();
        responseDto.setCode(ReturnMessage.SUCCESS.getCode());
        responseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        responseDto.setData(messageDataDto);
        return responseDto;
    }

    @Override
    public BaseResponseDto getUnreadMessageCount(BaseParamDto baseParamDto) {
        String loginName = baseParamDto.getBaseParam().getUserId();
        userMessageServices.generateUserMessages(loginName);
        long currentUnreadMessageCount = userMessageMapper.countUnreadMessagesByLoginName(loginName, Lists.newArrayList(MessageChannel.APP_MESSAGE));
        boolean existUnreadMessage = existUnreadMessage(loginName, currentUnreadMessageCount);
        MobileAppUnreadMessageCount messageCount = new MobileAppUnreadMessageCount();
        messageCount.setUnreadMessageCount(currentUnreadMessageCount);
        messageCount.setNewMessage(existUnreadMessage);
        BaseResponseDto responseDto = new BaseResponseDto();
        responseDto.setCode(ReturnMessage.SUCCESS.getCode());
        responseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        responseDto.setData(messageCount);
        return responseDto;
    }

    private UserMessageResponseDataDto fillMessageDataDto(String loginName, int index, int pageSize) {
        UserMessageResponseDataDto responseDataDto = new UserMessageResponseDataDto();
        List<UserMessageModel> userMessageModels = userMessageMapper.findMessagesByLoginName(loginName, Lists.newArrayList(MessageChannel.APP_MESSAGE), (index - 1) * pageSize, pageSize);
        long totalCount = userMessageMapper.countMessagesByLoginName(loginName, Lists.newArrayList(MessageChannel.APP_MESSAGE));
        List<UserMessageDto> userMessages = CollectionUtils.isEmpty(userMessageModels) ? new ArrayList<UserMessageDto>() :
                Lists.transform(userMessageModels, new Function<UserMessageModel, UserMessageDto>() {
                    @Override
                    public UserMessageDto apply(UserMessageModel model) {
                        return new UserMessageDto(model);
                    }
                });
        responseDataDto.setIndex(index);
        responseDataDto.setPageSize(pageSize);
        responseDataDto.setTotalCount(totalCount);
        responseDataDto.setMessages(userMessages);
        return responseDataDto;
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
