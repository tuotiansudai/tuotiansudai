package com.tuotiansudai.message.service;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.message.ManualMessage;
import com.tuotiansudai.message.dto.UserMessagePaginationItemDto;
import com.tuotiansudai.message.repository.mapper.MessageMapper;
import com.tuotiansudai.message.repository.mapper.UserMessageMapper;
import com.tuotiansudai.message.repository.model.MessageChannel;
import com.tuotiansudai.message.repository.model.MessageModel;
import com.tuotiansudai.message.repository.model.UserMessageModel;
import com.tuotiansudai.message.util.MessageUserGroupDecisionManager;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.util.PaginationUtil;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Autowired
    private MessageUserGroupDecisionManager messageUserGroupDecisionManager;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    public BasePaginationDataDto<UserMessagePaginationItemDto> getUserMessages(String loginName, String mobile, int index, int pageSize) {
        this.generateUserMessages(loginName, mobile, MessageChannel.WEBSITE);

        pageSize = pageSize < 1 ? 10 : pageSize;
        long count = userMessageMapper.countMessagesByLoginName(loginName, MessageChannel.WEBSITE);
        int offset = PaginationUtil.calculateOffset(index, pageSize, count);

        List<UserMessageModel> userMessageModels = userMessageMapper.findMessagesByLoginName(loginName, MessageChannel.WEBSITE, offset, pageSize);
        userMessageModels.stream().filter(userMessageModel -> Strings.isNullOrEmpty(userMessageModel.getContent())).forEach(userMessageModel -> {
            userMessageModel.setRead(true);
            userMessageModel.setReadTime(new Date());
            userMessageMapper.update(userMessageModel);
        });

        List<UserMessagePaginationItemDto> records = userMessageModels.stream().map(userMessageModel -> {
            MessageModel messageModel = messageMapper.findByIdBesidesDeleted(userMessageModel.getMessageId());
            return new UserMessagePaginationItemDto(userMessageModel, messageModel);
        }).collect(Collectors.toList());

        BasePaginationDataDto<UserMessagePaginationItemDto> dataDto = new BasePaginationDataDto<>(index, pageSize, count, records);
        dataDto.setStatus(true);

        return dataDto;
    }

    @Transactional
    public UserMessageModel readMessage(long userMessageId) {
        UserMessageModel userMessageModel = userMessageMapper.findById(userMessageId);
        if (userMessageModel != null && !userMessageModel.isRead()) {
            MessageModel messageModel = messageMapper.lockById(userMessageModel.getMessageId());
            messageModel.setReadCount(messageModel.getReadCount() + 1);
            messageMapper.update(messageModel);
            userMessageModel.setRead(true);
            userMessageModel.setReadTime(new Date());
            userMessageMapper.update(userMessageModel);
        }

        return userMessageModel;
    }

    public boolean readAll(String loginName, MessageChannel messageChannel) {
        List<UserMessageModel> userMessageModels = userMessageMapper.findMessagesByLoginName(loginName, messageChannel, null, null);
        userMessageModels.stream().filter(userMessageModel -> !userMessageModel.isRead()).forEach(userMessageModel -> {
            ((UserMessageService) AopContext.currentProxy()).readMessage(userMessageModel.getId());
        });
        return true;
    }

    public long getUnreadMessageCount(String loginName, String mobile, MessageChannel messageChannel) {
        List<MessageModel> unreadManualMessages = getUnreadManualMessages(loginName, mobile, messageChannel);
        long unreadCount = userMessageMapper.countUnreadMessagesByLoginName(loginName, messageChannel);
        return unreadManualMessages.size() + unreadCount;
    }

    public void generateUserMessages(String loginName, String mobile, MessageChannel messageChannel) {
        List<MessageModel> unreadManualMessages = getUnreadManualMessages(loginName, mobile, messageChannel);
        List<Long> messageIds = unreadManualMessages.stream().map(MessageModel::getId).collect(Collectors.toList());
        mqWrapperClient.sendMessage(MessageQueue.ManualMessage, new ManualMessage(loginName, messageIds));
    }

    public String getMessageWebURL(long userMessageId) {
        return messageMapper.findById(userMessageMapper.findById(userMessageId).getMessageId()).getWebUrl();
    }

    private List<MessageModel> getUnreadManualMessages(String loginName, String mobile, final MessageChannel messageChannel) {
        List<MessageModel> messages = this.messageMapper.findAssignableManualMessages(loginName);
        List<UserMessageModel> userMessageModels = userMessageMapper.findMessagesByLoginName(loginName, null, null, null);

        List<MessageModel> unreadManualMessages = Lists.newArrayList();
        messages.stream().filter(message -> message.getChannels().contains(messageChannel)).forEach(message -> {
            Optional<UserMessageModel> userMessageModelOptional = Iterators.tryFind(userMessageModels.iterator(), model -> model.getMessageId() == message.getId());
            if (!userMessageModelOptional.isPresent() && messageUserGroupDecisionManager.decide(loginName, mobile, message.getId())) {
                unreadManualMessages.add(message);
            }
        });

        return unreadManualMessages;
    }
}
